package com.example.tvtracker.Repository;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.tvtracker.Api.ApiBuilder;
import com.example.tvtracker.Api.ApiService;
import com.example.tvtracker.JsonModels.JsonTvShowSearchRoot;
import com.example.tvtracker.JsonModels.TvShowBasicInfo.JsonTvShow;
import com.example.tvtracker.JsonModels.TvShowBasicInfo.JsonTvShowBasicRoot;
import com.example.tvtracker.JsonModels.TvShowDetails.JsonTvShowFullRoot;
import com.example.tvtracker.MainActivity;
import com.example.tvtracker.Models.Basic.Resource;
import com.example.tvtracker.Models.Basic.Status;
import com.example.tvtracker.Models.Params.UpdateTvShowEpisodeWatchedFlagParams;
import com.example.tvtracker.Models.QueryModels.fromDbCall;
import com.example.tvtracker.Models.QueryModels.TvShowFull;
import com.example.tvtracker.Models.TvShow;
import com.example.tvtracker.Models.TvShowEpisode;
import com.example.tvtracker.Models.TvShowGenre;
import com.example.tvtracker.Models.TvShowPicture;
import com.example.tvtracker.Models.Params.UpdateTvShowDetailsParams;
import com.example.tvtracker.Models.Params.UpdateTvShowWatchingFlagParams;
import com.example.tvtracker.Models.TvShowSeason;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AppRepository {
    private AppDao appDao;


    private MutableLiveData<Resource<List<TvShow>>> discoverListObservable = new MutableLiveData<>();
    private MutableLiveData<Resource<List<TvShowFull>>> watchlistListObservable = new MutableLiveData<>();
    private MutableLiveData<Resource<TvShowFull>> detailObservable = new MutableLiveData<>();
    private MutableLiveData<Resource<TvShowSeason>> seasonObservable = new MutableLiveData<>();

    private MutableLiveData<List<TvShow>> allSearchTvShows;



    public AppRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        appDao = database.appDao();
        allSearchTvShows = new MutableLiveData<>();
    }

    public void fetchDiscoverData() {
        List<TvShow> loadingList = null;
        if(discoverListObservable.getValue() != null) {
            loadingList = discoverListObservable.getValue().data;
        }
        discoverListObservable.setValue(Resource.loading(loadingList));
        loadAllTvShowsFromDb();
        for (int i = 1; i <= MainActivity.TV_SHOW_MOST_POPULAR_PAGES_COUNT; i++) {
            getTvShowsFromWeb(i);
        }
    }

    public void fetchWatchlist() {
        List<TvShowFull> loadingList = null;
        if(watchlistListObservable.getValue() != null) {
            loadingList = watchlistListObservable.getValue().data;
        }
        watchlistListObservable.setValue(Resource.loading(loadingList));
        loadAllWatchlistTvShowsFromDb();
    }

    public void fetchTvShowDetails(int id) {
        TvShowFull loadingTvShow = null;
        if (detailObservable.getValue() != null) {
            loadingTvShow = detailObservable.getValue().data;
        }
        detailObservable.setValue(Resource.loading(loadingTvShow));
        loadTvShowDetailFromDb(id);
        getDetailsFromWeb(id);
    }

    public void fetchTvShowEpisodesBySeason(int id, int seasonNum){
        TvShowSeason loadingTvShowSeason = null;
        if(seasonObservable.getValue() != null){
            loadingTvShowSeason = seasonObservable.getValue().data;
        }
        seasonObservable.setValue(Resource.loading(loadingTvShowSeason));
        loadTvShowSeasonFromDb(id, seasonNum);

    }



    public MutableLiveData<Resource<List<TvShowFull>>> getWatchlistListObservable() {
        return watchlistListObservable;
    }

    public MutableLiveData<Resource<List<TvShow>>> getDiscoverListObservable() {
        return discoverListObservable;
    }

    public MutableLiveData<Resource<TvShowFull>> getDetailObservable() {
        return detailObservable;
    }

    public MutableLiveData<Resource<TvShowSeason>> getSeasonObservable() {
        return seasonObservable;
    }

    private void getTvShowsFromWeb(int pageNum) {
        Log.d("Fetch from web", "getTvShowsFromWeb: " + pageNum);
        ApiService apiService = ApiBuilder.getRetrofitInstance().create(ApiService.class);
        apiService.getTvShowsBasic(pageNum).enqueue(new Callback<JsonTvShowBasicRoot>() {
            @Override
            public void onResponse(Call<JsonTvShowBasicRoot> call, Response<JsonTvShowBasicRoot> response) {
                if(response.isSuccessful()){
                    setTvShowsListObservableStatus(Status.SUCCESS, null);
                    addTvShowsToDb(response.body().toTvShowArray());
                }else{
                    setTvShowsListObservableStatus(Status.ERROR, String.valueOf(response.code()));
                    switch (response.code()) {
                        case 404:
                            Log.d("Fetch from web","not found");
                            break;
                        case 500:
                            Log.d("Fetch from web","not logged in or server broken");
                            break;
                        default:
                            Log.d("Fetch from web", "unknown error");
                            break;
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonTvShowBasicRoot> call, Throwable t) {
                Log.d("Fetch from web", "error");
                setTvShowsListObservableStatus(Status.ERROR, t.getMessage());
            }
        });
    }

    private void getDetailsFromWeb(int id){
        Log.d("", "getDetailsFromWeb");
        ApiService apiService = ApiBuilder.getRetrofitInstance().create(ApiService.class);
        apiService.getTvShowDetailed(id).enqueue(new Callback<JsonTvShowFullRoot>() {
            @Override
            public void onResponse(Call<JsonTvShowFullRoot> call, Response<JsonTvShowFullRoot> response) {
                if(response.isSuccessful()){
                    setDetailObservableStatus(Status.SUCCESS, null);
                    addDetailsToDb(response.body().toDetail());
                }
            }

            @Override
            public void onFailure(Call<JsonTvShowFullRoot> call, Throwable t) {

            }
        });

    }

    private void addDetailsToDb(TvShowFull tvShowFull){
        Log.d("", "add details to db");
        new AsyncTask<TvShowFull, Void, Integer>() {
            @Override
            protected Integer doInBackground(TvShowFull... tvShowFulls) {
                TvShowFull tvShowFull = tvShowFulls[0];
                TvShow tvShow = tvShowFull.getTvShow();

                int id = tvShow.getTvShowId();

                List<TvShowEpisode> episodes = tvShowFull.getTvShowEpisodes();
                Collections.sort(episodes, new Comparator<TvShowEpisode>() {
                    @Override
                    public int compare(TvShowEpisode ep1, TvShowEpisode ep2) {
                        if (ep1.getSeasonNum() == ep2.getSeasonNum()) {
                            return 0;
                        } else if (ep1.getSeasonNum() > ep2.getSeasonNum()) {
                            return 1;
                        } else if (ep1.getSeasonNum() < ep2.getSeasonNum()) {
                            return -1;
                        }
                        return 0;
                    }
                });
                List<TvShowGenre> genres = tvShowFull.getTvShowGenres();
                List<TvShowPicture> pictures = tvShowFull.getTvShowPictures();

                List<TvShowEpisode> dbEpisodes = new ArrayList<>();
                List<TvShowGenre> dbGenres = new ArrayList<>();
                List<TvShowPicture> dbPictures = new ArrayList<>();

                TvShow dbTvShow = appDao.getTvShowById(id);
                dbEpisodes = appDao.getTvShowEpisodesById(id);
                dbGenres = appDao.getTvShowGenresById(id);
                dbPictures = appDao.getTvShowPicturesByTvShowId(id);

                if(dbTvShow != null) {
                    appDao.updateTvShowDetails(tvShow.getTvShowId(), tvShow.getTvShowDesc(), tvShow.getTvShowYoutubeLink(), tvShow.getTvShowRating());
                }else {
                    appDao.insertTvShow(new TvShow(tvShow.getTvShowId(), tvShow.getTvShowName(), tvShow.getTvShowStartDate(), tvShow.getTvShowEndDate(), tvShow.getTvShowCountry(),  tvShow.getTvShowNetwork(), tvShow.getTvShowStatus(), tvShow.getTvShowImagePath()));
                    appDao.updateTvShowDetails(tvShow.getTvShowId(), tvShow.getTvShowDesc(), tvShow.getTvShowYoutubeLink(), tvShow.getTvShowRating());
                }

                if(dbEpisodes.size() == 0) {
                    appDao.insertAllTvShowEpisodes(episodes);
                }

                if(dbGenres.size() == 0) {
                    appDao.insertAllTvShowGenres(genres);
                }

                if(dbPictures.size() == 0) {
                    appDao.insertAllTvShowPictures(pictures);
                }



                return tvShow.getTvShowId();
            }

            @Override
            protected void onPostExecute(Integer integer) {
                loadTvShowDetailFromDb(integer);
            }
        }.execute(tvShowFull);
    }

    private void addTvShowsToDb(List<TvShow> tvShows)  {
        Log.d("", "add tv shows to db" );
        new AsyncTask<List<TvShow>, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(List<TvShow>... params) {
                boolean needsUpdate = false;
                for(TvShow tvShow : params[0]) {
                    Long inserted = appDao.insertTvShow(tvShow);
                    if(inserted == -1) {
//                        long updated = appDao.updateTvShow(tvShow);
                        long updated = appDao.updateTvShow(tvShow.getTvShowId(), tvShow.getTvShowName(), tvShow.getTvShowStatus());
                        if(updated > 0){
                            needsUpdate = true;
                        }
                    }else {
                        needsUpdate = true;
                    }
                }
                return needsUpdate;
            }

            @Override
            protected void onPostExecute(Boolean needUpdate) {
                if(needUpdate) {
                    loadAllTvShowsFromDb();
                }
            }
        }.execute(tvShows);
    }

    private void loadAllTvShowsFromDb() {
        Log.d("", "load all tv shows from db");
        new AsyncTask<Void, Void, List<TvShow>>() {
            @Override
            protected List<TvShow> doInBackground(Void... voids) {
                return appDao.getAllTvShows();
            }

            @Override
            protected void onPostExecute(List<TvShow> tvShows) {
                if((tvShows != null) && tvShows.size()>0) {
                    setTvShowListObservableData(tvShows, null);
                }
            }
        }.execute();
    }
    private void loadTvShowDetailFromDb(int id) {
        Log.d("", "load tv show detail from db");
        new AsyncTask<Void, Void, TvShowFull>() {
            @Override
            protected TvShowFull doInBackground(Void... voids) {
                TvShow tvShow = appDao.getTvShowById(id);
//                List<TvShowEpisode> episodes = appDao.getTvShowEpisodesById(id);
                // need fix
//                int currentSeason = 1;
//                int maxSeason = appDao.getMaxSeasonByTvShowId(id);
//                for(int i = 1; i <= maxSeason; i++) {
//                    List<TvShowEpisode> currentEpisodes = appDao.getTvShowEpisodesByIdAndSeasonNum(id, currentSeason);
//                    episodes.addAll(currentEpisodes);
//                    currentSeason++;
//                    currentEpisodes.clear();
//                }
                List<TvShowEpisode> episodes = appDao.getTvShowEpisodesById(id);
                List<TvShowGenre> genres = appDao.getTvShowGenresById(id);
                List<TvShowPicture> pictures = appDao.getTvShowPicturesByTvShowId(id);
                TvShowFull tvShowFull = new TvShowFull(tvShow, episodes, genres, pictures);
                return tvShowFull;
            }

            @Override
            protected void onPostExecute(TvShowFull tvShowFull) {
                if(tvShowFull != null){
                    setDetailObservableData(tvShowFull, null);
                }
            }
        }.execute();
    }
    private void loadTvShowSeasonFromDb(int id, int seasonNum){
        Log.d("", "load tv show season from db");
        new AsyncTask<Void, Void, TvShowSeason>() {
            @Override
            protected TvShowSeason doInBackground(Void... voids) {
                List<TvShowEpisode> episodes = appDao.getTvShowEpisodesByIdAndSeasonNum(id, seasonNum);
                TvShowSeason season = new TvShowSeason(seasonNum, episodes);
                return season;
            }

            @Override
            protected void onPostExecute(TvShowSeason tvShowSeason) {
                if(tvShowSeason != null){
                    setSeasonObservableData(tvShowSeason, null);
                    setSeasonObservableStatus(com.example.tvtracker.Models.Basic.Status.SUCCESS, null);
                }
            }
        }.execute();

    }

    private void loadAllWatchlistTvShowsFromDb() {
        Log.d("", "load all tv shows from db");
        new AsyncTask<Void, Void, List<TvShowFull>>() {
            @Override
            protected List<TvShowFull> doInBackground(Void... voids) {
                List<TvShowFull> tvShowFullList = new ArrayList<>();
                List<TvShow> list = appDao.getWatchlistTvShows(MainActivity.TVSHOW_WATCHING_FLAG_YES);
                for(TvShow tvShow : list) {
                    int id = tvShow.getTvShowId();
                    List<TvShowEpisode> episodes = appDao.getTvShowEpisodesById(id);
                    List<TvShowGenre> genres = appDao.getTvShowGenresById(id);
                    List<TvShowPicture> pictures = appDao.getTvShowPicturesByTvShowId(id);
                    TvShowFull tvShowFull = new TvShowFull(tvShow, episodes, genres, pictures);
                    tvShowFullList.add(tvShowFull);
                }
                return tvShowFullList;
            }

            @Override
            protected void onPostExecute(List<TvShowFull> tvShows) {
                if((tvShows != null) && tvShows.size()>0) {
                    setWatchlistListObservableData(tvShows, null);
                }
            }
        }.execute();
    }

    private void setTvShowListObservableData(List<TvShow> mTvShowList, String message) {
        Log.d("setTvShowListObservable", "setTvShowListObservableData:");
        Status loadingStatus = Status.LOADING;
        if (discoverListObservable.getValue()!=null){
            loadingStatus= discoverListObservable.getValue().status;
        }
        switch (loadingStatus) {
            case LOADING:
                discoverListObservable.setValue(Resource.loading(mTvShowList));
                break;
            case ERROR:
                discoverListObservable.setValue(Resource.error(message,mTvShowList));
                break;
            case SUCCESS:
                discoverListObservable.setValue(Resource.success(mTvShowList));
                break;
        }
    }

    private void setTvShowsListObservableStatus(Status status, String message) {
        Log.d("setTvShowsListObservabl","setTvShowsListObservableStatus");
        List<TvShow> loadingList = null;
        if (discoverListObservable.getValue()!=null){
            loadingList= discoverListObservable.getValue().data;
        }
        switch (status) {
            case ERROR:
                discoverListObservable.setValue(Resource.error(message, loadingList));
                break;
            case LOADING:
                discoverListObservable.setValue(Resource.loading(loadingList));
                break;
            case SUCCESS:
//                if (loadingList!=null) {
                    discoverListObservable.setValue(Resource.success(loadingList));
//                }
                break;
        }

    }

    private void setWatchlistListObservableData(List<TvShowFull> mTvShowList, String message) {
        Log.d("setTvShowListObservable", "setTvShowListObservableData:");
        Status loadingStatus = Status.LOADING;
        if (watchlistListObservable.getValue()!=null){
            loadingStatus=watchlistListObservable.getValue().status;
        }
        switch (loadingStatus) {
            case LOADING:
                watchlistListObservable.setValue(Resource.loading(mTvShowList));
                break;
            case ERROR:
                watchlistListObservable.setValue(Resource.error(message,mTvShowList));
                break;
            case SUCCESS:
                watchlistListObservable.setValue(Resource.success(mTvShowList));
                break;
        }
    }

    private void setWatchlistListObservableStatus(Status status, String message) {
        Log.d("setTvShowsListObservabl","setTvShowsListObservableStatus");
        List<TvShowFull> loadingList = null;
        if (watchlistListObservable.getValue()!=null){
            loadingList=watchlistListObservable.getValue().data;
        }
        switch (status) {
            case ERROR:
                watchlistListObservable.setValue(Resource.error(message, loadingList));
                break;
            case LOADING:
                watchlistListObservable.setValue(Resource.loading(loadingList));
                break;
            case SUCCESS:
                if (loadingList!=null) {
                    watchlistListObservable.setValue(Resource.success(loadingList));
                }
                break;
        }

    }

    private void setDetailObservableData(TvShowFull tvShowFull, String message) {
        Log.d("setDetailObservableData", "setDetailListObservableData:");
        Status loadingStatus = Status.LOADING;
        if (detailObservable.getValue()!=null){
            loadingStatus=detailObservable.getValue().status;
        }
        switch (loadingStatus) {
            case LOADING:
                detailObservable.setValue(Resource.loading(tvShowFull));
                break;
            case ERROR:
                detailObservable.setValue(Resource.error(message, tvShowFull));
                break;
            case SUCCESS:
                detailObservable.setValue(Resource.success(tvShowFull));
                break;
        }
    }

    private void setDetailObservableStatus(Status status, String message) {
        Log.d("setDetailObservableStat","setDetailListObservableStatus");
        TvShowFull loadingList = null;
        if (detailObservable.getValue()!=null){
            loadingList=detailObservable.getValue().data;
        }
        switch (status) {
            case ERROR:
                detailObservable.setValue(Resource.error(message, loadingList));
                break;
            case LOADING:
                detailObservable.setValue(Resource.loading(loadingList));
                break;
            case SUCCESS:
                if (loadingList!=null) {
                    detailObservable.setValue(Resource.success(loadingList));
                }
                break;
        }

    }

    private void setSeasonObservableData(TvShowSeason tvShowSeason, String message) {
        Log.d("setSeasonObservableData", "setSeasonObservableData:");
        Status loadingStatus = Status.LOADING;
        if (seasonObservable.getValue()!=null){
            loadingStatus=seasonObservable.getValue().status;
        }
        switch (loadingStatus) {
            case LOADING:
                seasonObservable.setValue(Resource.loading(tvShowSeason));
                break;
            case ERROR:
                seasonObservable.setValue(Resource.error(message,tvShowSeason));
                break;
            case SUCCESS:
                seasonObservable.setValue(Resource.success(tvShowSeason));
                break;
        }
    }

    private void setSeasonObservableStatus(Status status, String message) {
        Log.d("setSeasonObservableStat","setSeasonObservableStatus");
        TvShowSeason loadingList = null;
        if (seasonObservable.getValue()!=null){
            loadingList=seasonObservable.getValue().data;
        }
        switch (status) {
            case ERROR:
                seasonObservable.setValue(Resource.error(message, loadingList));
                break;
            case LOADING:
                seasonObservable.setValue(Resource.loading(loadingList));
                break;
            case SUCCESS:
                if (loadingList!=null) {
                    seasonObservable.setValue(Resource.success(loadingList));
                }
                break;
        }

    }

    public void updateTvShowWatchingFlag(UpdateTvShowWatchingFlagParams params) {
        new AsyncTask<Void,Void, Void>(){
            @Override
            protected Void doInBackground(Void... voids) {
                int id = params.getId();
                String flag = params.getFlag();
                appDao.updateTvShowWatchingFlag(id, flag);
                return null;
            }
        }.execute();
    }

    public void updateTvShowEpisodeIsWatchedFlag(UpdateTvShowEpisodeWatchedFlagParams params) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                int id = params.getId();
                boolean flag = params.getFlag();
                appDao.updateTvShowEpisodeWatchedFlag(id, flag);
                return null;
            }
        }.execute();
    }



    //-------------------------------------------------


    public LiveData<List<TvShow>> getAllSearchTvShows() {
        return allSearchTvShows;
    }


    //Api calls

    public void searchTvShow(String searchWord, int pageNum) {
        ApiService apiService = ApiBuilder.getRetrofitInstance().create(ApiService.class);
        Call<JsonTvShowSearchRoot> jsonTvShowSearchRootCall = apiService.getTvShowSearch(searchWord, pageNum);
        jsonTvShowSearchRootCall.enqueue(new Callback<JsonTvShowSearchRoot>() {
            @Override
            public void onResponse(Call<JsonTvShowSearchRoot> call, Response<JsonTvShowSearchRoot> response) {
                JsonTvShowSearchRoot  root = response.body();
                searchTvShowsCallback(root);
                Log.e("BIGFAIL", String.valueOf(response));
            }

            @Override
            public void onFailure(Call<JsonTvShowSearchRoot> call, Throwable t) {
                Log.e("BIGFAIL", t.getMessage());
            }
        });
    }

    private void searchTvShowsCallback(JsonTvShowSearchRoot root){
        List<TvShow> searchedTvShows = new ArrayList<>();
        for (int i = 0; i < root.getTvShows().size(); i++) {
            JsonTvShow jsonTvShow = root.getTvShows().get(i);
            TvShow tvShow = new TvShow(jsonTvShow.getId(), jsonTvShow.getName(), jsonTvShow.getStartDate(), jsonTvShow.getEndDate(), jsonTvShow.getCountry(), jsonTvShow.getNetwork(), jsonTvShow.getStatus(), jsonTvShow.getImageThumbnailPath());
            searchedTvShows.add(tvShow);
        }
        allSearchTvShows.setValue(searchedTvShows);
    }

    //Help Functions
    public void clearSearchedTvShows() {
        List<TvShow> emptyTvShows = new ArrayList<>();
        allSearchTvShows.postValue(emptyTvShows);
    }

    //TvShow

    public void insertTvShows(List<TvShow> tvShows) {
        new InsertTvShowsAsyncTask(appDao).execute(tvShows);
    }


    public TvShow getTvShowById(int Id) {
        try {
            return new GetTvShowAsyncTask(appDao).execute(Id).get();
        } catch (Exception e) {
            Log.e("Error:", e.getMessage());
        }
        return null;
    }

    public void insertTvShow(TvShow tvShow) {

        new InsertTvShowAsyncTask(appDao).execute(tvShow);
    }

    public void updateTvShow(TvShow tvShow) {
        new UpdateTvShowAsyncTask(appDao).execute(tvShow);
    }

    private void updateTvShowDetails(UpdateTvShowDetailsParams params) {
        new UpdateTvShowDetailsAsyncTasks(appDao).execute(params);
    }



    public void deleteTvShow(int id) {
        new DeleteTvShowAsyncTask(appDao).execute(id);
    }


    public void deleteAllTvShows() {
        new DeleteAllTvShows(appDao).execute();
    }

    //TvShowPicture
    private void insertTvShowPicture(TvShowPicture tvShowPicture) {
        new InsertTvShowPictureAsyncTask(appDao).execute(tvShowPicture);
    }

    public List<TvShowPicture> getTvShowPicturesByShowId(int showId) {
        try {
            return new GetTvShowPicturesAsyncTask(appDao).execute(showId).get();
        } catch (Exception e) {
            Log.e("Error:", e.getMessage());
        }
        return null;
    }

    //TvShowEpisode
    private void insertTvShowEpisode(TvShowEpisode tvShowEpisode) {
        new InsertTvShowEpisodeAsyncTask(appDao).execute(tvShowEpisode);
    }

    public List<TvShowEpisode> getTvShowEpisodesById(int showId) {
        try {
            return new GetTvShowEpisodesAsyncTask(appDao).execute(showId).get();
        } catch (Exception e) {
            Log.e("Error:", e.getMessage());
        }
        return null;
    }

    //TvShowGenre

    private void insertTvShowGenre(TvShowGenre tvShowGenre) {
        new InsertTvShowGenreAsyncTask(appDao).execute(tvShowGenre);
    }

    public List<TvShowGenre> getTvShowGenresById(int showId) {
        try {
            return new GetTvShowGenresAsyncTask(appDao).execute(showId).get();
        } catch (Exception e) {
            Log.e("Error:", e.getMessage());
        }
        return null;
    }

    //DetailsFragment
    public List<fromDbCall> getTvShowWithPicturesAndEpisodesById(int tvShowId) {
        try {
            return new GetTvShowWithPicturesAndEpisodesByIdAsyncTask(appDao).execute(tvShowId).get();
        } catch (Exception e) {
            Log.e("Error:", e.getMessage());
        }
        return null;
    }

    //TvShow AsyncTasks

    private static class InsertTvShowsAsyncTask extends AsyncTask<List<TvShow>, Void, Void> {

        private AppDao appDao;

        InsertTvShowsAsyncTask(AppDao appDao) {
            this.appDao = appDao;
        }

        @Override
        protected Void doInBackground(final List<TvShow>... params) {
            appDao.insertTvShows(params[0]);
            return null;
        }
    }




    private static class GetTvShowAsyncTask extends AsyncTask<Integer, Void, TvShow> {
        private AppDao appDao;

        private GetTvShowAsyncTask(AppDao appDao) {
            this.appDao = appDao;
        }

        @Override
        protected TvShow doInBackground(Integer... integers) {
            return appDao.getTvShowById(integers[0]);
        }

        @Override
        protected void onPostExecute(TvShow tvShow) {
            super.onPostExecute(tvShow);
        }
    }

    private static class InsertTvShowAsyncTask extends AsyncTask<TvShow, Void, Void> {
        private AppDao appDao;

        private InsertTvShowAsyncTask(AppDao appDao) {
            this.appDao = appDao;
        }

        @Override
        protected Void doInBackground(TvShow... tvShows) {
            appDao.insertTvShow(tvShows[0]);
            return null;
        }
    }

    private static class UpdateTvShowAsyncTask extends AsyncTask<TvShow, Void, Void> {
        private AppDao appDao;

        private UpdateTvShowAsyncTask(AppDao appDao) {
            this.appDao = appDao;
        }

        @Override
        protected Void doInBackground(TvShow... tvShows) {
            TvShow tvShow = tvShows[0];
            appDao.updateTvShow(tvShow.getTvShowId(), tvShow.getTvShowName(), tvShow.getTvShowStatus());
            return null;
        }
    }

    private static class UpdateTvShowDetailsAsyncTasks extends AsyncTask<UpdateTvShowDetailsParams, Void, Void> {
        private AppDao appDao;

        private UpdateTvShowDetailsAsyncTasks(AppDao appDao) {
            this.appDao = appDao;
        }

        @Override
        protected Void doInBackground(UpdateTvShowDetailsParams... params) {
            UpdateTvShowDetailsParams parameters = params[0];
            appDao.updateTvShowDetails(parameters.getId(), parameters.getDesc(), parameters.getYoutubeLink(), parameters.getRating());
            return null;
        }
    }

    private static class UpdateTvShowWatchingFlagAsyncTask extends AsyncTask<UpdateTvShowWatchingFlagParams, Void, Void> {
        private AppDao appDao;

        private UpdateTvShowWatchingFlagAsyncTask(AppDao appDao) {
            this.appDao = appDao;
        }

        @Override
        protected Void doInBackground(UpdateTvShowWatchingFlagParams... params) {
            int id = params[0].getId();
            String flag = params[0].getFlag();
            appDao.updateTvShowWatchingFlag(id, flag);
            return null;
        }
    }

    private static class DeleteTvShowAsyncTask extends AsyncTask<Integer, Void, Void> {
        private AppDao appDao;

        private DeleteTvShowAsyncTask(AppDao appDao) {
            this.appDao = appDao;
        }

        @Override
        protected Void doInBackground(Integer... integers) {
            appDao.deleteTvShowById(integers[0]);
            return null;
        }
    }

    private static class DeleteAllTvShows extends AsyncTask<Void, Void, Void> {
        private AppDao appDao;

        private DeleteAllTvShows(AppDao appDao) {
            this.appDao = appDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            appDao.deleteAllTvShows();
            return null;
        }
    }

    //TvShowPicture AsyncTasks
    private static class InsertTvShowPictureAsyncTask extends AsyncTask<TvShowPicture, Void, Void> {
        private AppDao appDao;

        InsertTvShowPictureAsyncTask(AppDao appDao) {
            this.appDao = appDao;
        }

        @Override
        protected Void doInBackground(TvShowPicture... tvShowPictures) {
            appDao.insertTvShowPicture(tvShowPictures[0]);
            return null;
        }
    }

    private static class GetTvShowPicturesAsyncTask extends AsyncTask<Integer, Void, List<TvShowPicture>> {
        List<TvShowPicture> tvShowPictureList;
        private AppDao appDao;

        GetTvShowPicturesAsyncTask(AppDao appDao) {
            this.appDao = appDao;
        }

        @Override
        protected List<TvShowPicture> doInBackground(Integer... integers) {
            this.tvShowPictureList = appDao.getTvShowPicturesByTvShowId(integers[0]);
            return tvShowPictureList;
        }

        @Override
        protected void onPostExecute(List<TvShowPicture> tvShowPictures) {
            super.onPostExecute(tvShowPictures);
        }
    }

    //TvShowEpisode AsyncTasks
    private static class InsertTvShowEpisodeAsyncTask extends AsyncTask<TvShowEpisode, Void, Void> {
        private AppDao appDao;

        InsertTvShowEpisodeAsyncTask(AppDao appDao) {
            this.appDao = appDao;
        }

        @Override
        protected Void doInBackground(TvShowEpisode... tvShowEpisodes) {
            appDao.insertTvShowEpisode(tvShowEpisodes[0]);
            return null;
        }
    }

    private static class GetTvShowEpisodesAsyncTask extends AsyncTask<Integer, Void, List<TvShowEpisode>> {
        private AppDao appDao;

        GetTvShowEpisodesAsyncTask(AppDao appDao) {
            this.appDao = appDao;
        }

        @Override
        protected List<TvShowEpisode> doInBackground(Integer... integers) {
            return appDao.getTvShowEpisodesById(integers[0]);
        }

        @Override
        protected void onPostExecute(List<TvShowEpisode> tvShowEpisodes) {
            super.onPostExecute(tvShowEpisodes);
        }
    }

    //TvShowGenre AsyncTasks
    private static class InsertTvShowGenreAsyncTask extends AsyncTask<TvShowGenre, Void, Void> {
        private AppDao appDao;

        InsertTvShowGenreAsyncTask(AppDao appDao) {
            this.appDao = appDao;
        }

        @Override
        protected Void doInBackground(TvShowGenre... tvShowGenres) {
            appDao.insertTvShowGenre(tvShowGenres[0]);
            return null;
        }
    }

    private static class GetTvShowGenresAsyncTask extends AsyncTask<Integer, Void, List<TvShowGenre>> {
        private AppDao appDao;

        GetTvShowGenresAsyncTask(AppDao appDao) {
            this.appDao = appDao;
        }

        @Override
        protected List<TvShowGenre> doInBackground(Integer... integers) {
            return appDao.getTvShowGenresById(integers[0]);
        }

        @Override
        protected void onPostExecute(List<TvShowGenre> tvShowGenres) {
            super.onPostExecute(tvShowGenres);
        }
    }

    //DetailsFragment AsyncTasks
    private static class GetTvShowWithPicturesAndEpisodesByIdAsyncTask extends AsyncTask<Integer, Void, List<fromDbCall>> {
        private AppDao appDao;

        GetTvShowWithPicturesAndEpisodesByIdAsyncTask(AppDao appDao) {
            this.appDao = appDao;
        }

        @Override
        protected List<fromDbCall> doInBackground(Integer... integers) {
            return appDao.getTvShowWithPicturesAndEpisodesById(integers[0]);
        }
    }

    private static class  SearchK extends AsyncTask<TvShow, Void, Void> {

        @Override
        protected Void doInBackground(TvShow... tvShows) {

            return null;
        }
    }





}
