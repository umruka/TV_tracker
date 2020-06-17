package com.example.tvtracker.Repository;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.tvtracker.Api.ApiBuilder;
import com.example.tvtracker.Api.ApiService;
import com.example.tvtracker.JsonModels.JsonTvShowSearchRoot;
import com.example.tvtracker.JsonModels.TvShowBasicInfo.JsonTvShow;
import com.example.tvtracker.JsonModels.TvShowBasicInfo.JsonTvShowBasicRoot;
import com.example.tvtracker.JsonModels.TvShowDetails.JsonTvShowFullRoot;
import com.example.tvtracker.MainActivity;
import com.example.tvtracker.Models.Basic.NetworkBoundResource;
import com.example.tvtracker.Models.Basic.Resource;
import com.example.tvtracker.Models.Basic.Status;
import com.example.tvtracker.Models.MultiTaskHandler;
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


    private MutableLiveData<List<TvShowFull>> watchlistListObservable = new MutableLiveData<>();
    private MutableLiveData<Resource<TvShowFull>> detailObservable = new MutableLiveData<>();
    private MutableLiveData<TvShowSeason> seasonObservable = new MutableLiveData<>();
    private MutableLiveData<Boolean> syncState = new MutableLiveData<>();


    private MutableLiveData<List<TvShow>> allSearchTvShows;
    private MutableLiveData<List<TvShowEpisode>> last30daysEpisodes = new MutableLiveData<>();

    final MultiTaskHandler handler;

    public AppRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        appDao = database.appDao();
        allSearchTvShows = new MutableLiveData<>();


        handler = new MultiTaskHandler(MainActivity.TV_SHOW_MOST_POPULAR_PAGES_COUNT) {
            @Override
            protected void onAllTasksCompleted() {
                syncState.setValue(true);
            }
        };
    }

    public void fetchAllData(){
        ApiService apiService = ApiBuilder.getRetrofitInstance().create(ApiService.class);
        for (int i = 1; i <= MainActivity.TV_SHOW_MOST_POPULAR_PAGES_COUNT; i++) {
            apiService.getTvShowsBasic(i).enqueue(new Callback<JsonTvShowBasicRoot>() {
                @Override
                public void onResponse(Call<JsonTvShowBasicRoot> call, Response<JsonTvShowBasicRoot> response) {
                    if(response.isSuccessful()){
//                    setTvShowsListObservableStatus(Status.SUCCESS, null);
                        addTvShowsToDb(toTvShowArray(response.body()));
                    }
                }

                @Override
                public void onFailure(Call<JsonTvShowBasicRoot> call, Throwable t) {
                    Log.d("Fetch from web", "error");
//                setTvShowsListObservableStatus(Status.ERROR, t.getMessage());
                }
            });
        }


    }

    public List<TvShow> toTvShowArray(JsonTvShowBasicRoot root) {
        List<TvShow> returnTvShows = new ArrayList<>();
        List<JsonTvShow> TVShows = root.getTVShows();
        for(int i=0;i<TVShows.size();i++){

            JsonTvShow urlTvShow = TVShows.get(i);;

            int tvShowId = urlTvShow.getId();
            String tvShowName = urlTvShow.getName();
            String tvShowStatus = urlTvShow.getStatus();
            String tvShowStartDate = urlTvShow.getStartDate();
            String tvShowEndDate = urlTvShow.getEndDate();
            String tvShowCountry = urlTvShow.getCountry();
            String tvShowNetwork = urlTvShow.getNetwork();
            String tvShowImage = urlTvShow.getImageThumbnailPath();


            TvShow tvShow = new TvShow(tvShowId, tvShowName, tvShowStartDate, tvShowEndDate, tvShowCountry, tvShowNetwork, tvShowStatus, tvShowImage);
            returnTvShows.add(tvShow);

        }
        return  returnTvShows;
    }


    public LiveData<Boolean> getSyncState() {
        return syncState;
    }

    public LiveData<List<TvShow>> fetchDiscover2() {
     return appDao.getAllTvShows();
    }

    public LiveData<Resource<TvShowFull>> fetchTvShowDetails2(int id){
        return new NetworkBoundResource<TvShowFull, JsonTvShowFullRoot>() {
            @Override
            protected void saveCallResult(@NonNull JsonTvShowFullRoot item) {
            apiFunction(item);
            }

            @Override
            protected boolean shouldFetch(@Nullable TvShowFull data) {
                return true;
            }

            @NonNull
            @Override
            protected LiveData<TvShowFull> loadFromDb() {
                return loadTvShowDetailFromDb(id);
            }

            @NonNull
            @Override
            protected Call<JsonTvShowFullRoot> createCall() {
                ApiService apiService = ApiBuilder.getRetrofitInstance().create(ApiService.class);
                return apiService.getTvShowDetailed(id);
            }
        }.getAsLiveData();
    }

    public void fetchWatchlist() {
        loadAllWatchlistTvShowsFromDb();
    }

    public void fetchTvShowDetails(int id) {
        getDetailsFromWeb(id);
    }

    public void fetchTvShowEpisodesBySeason(int id, int seasonNum){
        loadTvShowSeasonFromDb(id, seasonNum);
    }


    public void fetchEpisodesForCalendar(){
//        List<TvShowEpisode> episodes = new ArrayList<>();
//        last30daysEpisodes.setValue(episodes);
        new AsyncTask<Void, Void, List<TvShowEpisode>>(){
            @Override
            protected List<TvShowEpisode> doInBackground(Void... voids) {
            List<TvShowEpisode> episodes =appDao.getTvShowEpisodesForLast30Days();
                return episodes;
            }

            @Override
            protected void onPostExecute(List<TvShowEpisode> tvShowEpisodes) {
                if(tvShowEpisodes != null){
                 last30daysEpisodes.postValue(tvShowEpisodes);
                }
            }
        }.execute();
    }

    public MutableLiveData<List<TvShowEpisode>> getLast30daysEpisodes() {
        return last30daysEpisodes;
    }

    public MutableLiveData<List<TvShowFull>> getWatchlistListObservable() {
        return watchlistListObservable;
    }

    public MutableLiveData<Resource<TvShowFull>> getDetailObservable() {
        return detailObservable;
    }

    public LiveData<TvShowSeason> getSeasonObservable() {
        return seasonObservable;
    }
/*
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
*/
    private void getDetailsFromWeb(int id){
        Log.d("", "getDetailsFromWeb");
        ApiService apiService = ApiBuilder.getRetrofitInstance().create(ApiService.class);
        apiService.getTvShowDetailed(id).enqueue(new Callback<JsonTvShowFullRoot>() {
            @Override
            public void onResponse(Call<JsonTvShowFullRoot> call, Response<JsonTvShowFullRoot> response) {
                if(response.isSuccessful()){
                    setDetailObservableStatus(Status.SUCCESS, null);
                    addDetailsToDb(response.body());
                }
            }

            @Override
            public void onFailure(Call<JsonTvShowFullRoot> call, Throwable t) {

            }
        });

    }

    private void apiFunction(JsonTvShowFullRoot item){
        Log.d("", "add details to db");
        TvShowFull tvShowFull = item.toDetail();
        TvShow tvShow = tvShowFull.getTvShow();

        int id = tvShow.getTvShowId();
        List<TvShowEpisode> episodes = tvShowFull.getTvShowEpisodes();
//                /*
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
        for(TvShowEpisode episode : episodes){
            String shortDate = episode.getEpisodeAirDate().substring(0, 10);
            if(shortDate.matches("\\d{4}-\\d{2}-\\d{2}")) {
                episode.setEpisodeAirDate(shortDate);
            }else{
                episode.setEpisodeAirDate("");
            }
        }
        List<TvShowGenre> genres = tvShowFull.getTvShowGenres();
        List<TvShowPicture> pictures = tvShowFull.getTvShowPictures();

        List<TvShowEpisode> dbEpisodes = new ArrayList<>();
        List<TvShowGenre> dbGenres = new ArrayList<>();
        List<TvShowPicture> dbPictures = new ArrayList<>();

        TvShow dbTvShow = appDao.getTvShowByApiId(id);
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
    }

    private void addDetailsToDb(JsonTvShowFullRoot item){
        Log.d("", "add details to db");
        new AsyncTask<JsonTvShowFullRoot, Void, Void>() {
            @Override
            protected Void doInBackground(JsonTvShowFullRoot... jsonTvShowFullRoots) {
                apiFunction(jsonTvShowFullRoots[0]);
                return null;
            }
        }.execute(item);
    }

    private void addTvShowsToDb(List<TvShow> tvShows)  {
        Log.d("", "add tv shows to db" );
        new  AsyncTask<List<TvShow>, Void, Void>() {
            @Override
            protected synchronized Void doInBackground(List<TvShow>... params) {
                for (TvShow tvShow : params[0]) {
                    if (appDao.getTvShowByApiId(tvShow.getTvShowId()) != null) {
                        //update tv show fix all slots
                        appDao.updateTvShow(tvShow.getId(), tvShow.getTvShowName(), tvShow.getTvShowStatus(), tvShow.getTvShowStartDate(), tvShow.getTvShowEndDate(), tvShow.getTvShowCountry(), tvShow.getTvShowNetwork(), tvShow.getTvShowImagePath());
                    } else {
                        appDao.insertTvShow(tvShow);
                    }
                }
                return null;

            }

            @Override
            protected void onPostExecute(Void aVoid) {
            handler.taskComplete();
            }
        }.execute(tvShows);
    }
/*
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

 */
    private LiveData<TvShowFull> loadTvShowDetailFromDb(int id) {
        MutableLiveData<TvShowFull> fullMutableLiveData = new MutableLiveData<>();
        Log.d("", "load tv show detail from db");
        new AsyncTask<Void, Void, TvShowFull>() {
            @Override
            protected TvShowFull doInBackground(Void... voids) {
                TvShow tvShow = appDao.getTvShowByApiId(id);
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
                fullMutableLiveData.postValue(tvShowFull);
            }
        }.execute();
        return fullMutableLiveData;
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
                seasonObservable.postValue(tvShowSeason);
            }
        }.execute();

    }
///*
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
                    watchlistListObservable.postValue(tvShows);
                }
            }
        }.execute();
    }

// */
/*
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
*/
/*
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
*/
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



    public void updateTvShowWatchingFlag(UpdateTvShowWatchingFlagParams params) {
        new AsyncTask<Void,Void, Void>(){
            @Override
            protected Void doInBackground(Void... voids) {
                int id = params.getId();
                boolean flag = params.getFlag();
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
            return appDao.getTvShowByApiId(integers[0]);
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
            appDao.updateTvShow(tvShow.getTvShowId(), tvShow.getTvShowName(), tvShow.getTvShowStatus(), tvShow.getTvShowStartDate(), tvShow.getTvShowEndDate(), tvShow.getTvShowCountry(), tvShow.getTvShowNetwork(), tvShow.getTvShowImagePath());
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
            boolean flag = params[0].getFlag();
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
