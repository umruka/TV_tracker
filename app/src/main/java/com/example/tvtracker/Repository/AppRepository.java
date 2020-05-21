package com.example.tvtracker.Repository;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.tvtracker.Api.ApiBuilder;
import com.example.tvtracker.Api.ApiService;
import com.example.tvtracker.JsonModels.JsonTvShowSearchRoot;
import com.example.tvtracker.JsonModels.TvShowBasicInfo.JsonTvShowBasicInfo;
import com.example.tvtracker.JsonModels.TvShowBasicInfo.JsonTvShowBasicInfoRoot;
import com.example.tvtracker.JsonModels.TvShowDetails.JsonTvShowDetailsEpisode;
import com.example.tvtracker.JsonModels.TvShowDetails.JsonTvShowDetailsInfo;
import com.example.tvtracker.JsonModels.TvShowDetails.JsonTvShowDetailsInfoRoot;
import com.example.tvtracker.MainActivity;
import com.example.tvtracker.Models.Basic.Resource;
import com.example.tvtracker.Models.Basic.Status;
import com.example.tvtracker.Models.QueryModels.TvShowFull;
import com.example.tvtracker.Models.QueryModels.TvShowTest;
import com.example.tvtracker.Models.TvShow;
import com.example.tvtracker.Models.TvShowEpisode;
import com.example.tvtracker.Models.TvShowGenre;
import com.example.tvtracker.Models.TvShowPicture;
import com.example.tvtracker.Models.Params.UpdateTvShowDetailsParams;
import com.example.tvtracker.Models.Params.UpdateTvShowWatchingFlagParams;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AppRepository {
    private AppDao appDao;


    private MutableLiveData<Resource<List<TvShow>>> tvShowListObservable = new MutableLiveData<>();
    private MutableLiveData<Resource<List<TvShow>>> watchlistListObservable = new MutableLiveData<>();

    private MutableLiveData<List<TvShow>> allSearchTvShows;
    private MutableLiveData<List<TvShowFull>> searchedTvShow;



    public AppRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        appDao = database.appDao();
//        allTvShows = appDao.getAllTvShows();
//        allWatchingTvShows = watchlist();
        allSearchTvShows = new MutableLiveData<>();
    }

    public void fetchData() {
        List<TvShow> loadingList = null;
        if(tvShowListObservable.getValue() != null) {
            loadingList = tvShowListObservable.getValue().data;
        }
        tvShowListObservable.setValue(Resource.loading(loadingList));
        loadAllTvShowsFromDb();
//        for (int i = 0; i < 5; i++) {
            getTvShowsFromWeb(1);
//        }
    }

    public void fetchWatchlist() {
        List<TvShow> loadingList = null;
        if(watchlistListObservable.getValue() != null) {
            loadingList = watchlistListObservable.getValue().data;
        }
        watchlistListObservable.setValue(Resource.loading(loadingList));
        loadAllWatchlistTvShowsFromDb();
    }

    public MutableLiveData<Resource<List<TvShow>>> getWatchlistListObservable() {
        return watchlistListObservable;
    }

    public MutableLiveData<Resource<List<TvShow>>> getTvShowListObservable() {
        return tvShowListObservable;
    }

    private void getTvShowsFromWeb(int pageNum) {
        Log.d("Fetch from web", "getTvShowsFromWeb");
        ApiService apiService = ApiBuilder.getRetrofitInstance().create(ApiService.class);
        apiService.getTvShowsBasic(pageNum).enqueue(new Callback<JsonTvShowBasicInfoRoot>() {
            @Override
            public void onResponse(Call<JsonTvShowBasicInfoRoot> call, Response<JsonTvShowBasicInfoRoot> response) {
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
            public void onFailure(Call<JsonTvShowBasicInfoRoot> call, Throwable t) {
                Log.d("Fetch from web", "error");
                setTvShowsListObservableStatus(Status.ERROR, t.getMessage());
            }
        });
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

    private void loadAllWatchlistTvShowsFromDb() {
        Log.d("", "load all tv shows from db");
        new AsyncTask<Void, Void, List<TvShow>>() {
            @Override
            protected List<TvShow> doInBackground(Void... voids) {
                return appDao.getWatchlistTvShows(MainActivity.TVSHOW_WATCHING_FLAG_YES);
            }

            @Override
            protected void onPostExecute(List<TvShow> tvShows) {
                if((tvShows != null) && tvShows.size()>0) {
                    setWatchlistListObservableData(tvShows, null);
                }
            }
        }.execute();
    }

    /**
     * This method changes the observable's LiveData data without changing the status
     * @param mTvShowList the data that need to be updated
     * @param message optional message for error
     */
    private void setTvShowListObservableData(List<TvShow> mTvShowList, String message) {
        Log.d("setTvShowListObservable", "setRecipesListObservableData:");
        Status loadingStatus = Status.LOADING;
        if (tvShowListObservable.getValue()!=null){
            loadingStatus=tvShowListObservable.getValue().status;
        }
        switch (loadingStatus) {
            case LOADING:
                tvShowListObservable.setValue(Resource.loading(mTvShowList));
                break;
            case ERROR:
                tvShowListObservable.setValue(Resource.error(message,mTvShowList));
                break;
            case SUCCESS:
                tvShowListObservable.setValue(Resource.success(mTvShowList));
                break;
        }
    }

    /**
     * This method changes the observable's LiveData status without changing the data
     * @param status The new status of LiveData
     * @param message optional message for error
     */
    private void setTvShowsListObservableStatus(Status status, String message) {
        Log.d("setTvShowsListObservabl","setRecipesListObservableStatus");
        List<TvShow> loadingList = null;
        if (tvShowListObservable.getValue()!=null){
            loadingList=tvShowListObservable.getValue().data;
        }
        switch (status) {
            case ERROR:
                tvShowListObservable.setValue(Resource.error(message, loadingList));
                break;
            case LOADING:
                tvShowListObservable.setValue(Resource.loading(loadingList));
                break;
            case SUCCESS:
                if (loadingList!=null) {
                    tvShowListObservable.setValue(Resource.success(loadingList));
                }
                break;
        }

    }

    private void setWatchlistListObservableData(List<TvShow> mTvShowList, String message) {
        Log.d("setTvShowListObservable", "setRecipesListObservableData:");
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

    /**
     * This method changes the observable's LiveData status without changing the data
     * @param status The new status of LiveData
     * @param message optional message for error
     */
    private void setWatchlistListObservableStatus(Status status, String message) {
        Log.d("setTvShowsListObservabl","setRecipesListObservableStatus");
        List<TvShow> loadingList = null;
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



    //-------------------------------------------------


    public LiveData<List<TvShow>> getAllSearchTvShows() {
        return allSearchTvShows;
    }


    public void syncDuo() {

    }

    //Api calls
    public void insertMostPopularTvShowsBasicInfo(int pageNum) {

        ApiService apiService = ApiBuilder.getRetrofitInstance().create(ApiService.class);
            Call<JsonTvShowBasicInfoRoot> jsonTvShowBasicRootCall = apiService.getTvShowsBasic(pageNum);
            jsonTvShowBasicRootCall.enqueue(new Callback<JsonTvShowBasicInfoRoot>() {
                @Override
                public void onResponse(Call<JsonTvShowBasicInfoRoot> call, Response<JsonTvShowBasicInfoRoot> response) {

                    JsonTvShowBasicInfoRoot jsonTvShowBasicElement = response.body();
                    generateTvShowsCallback(jsonTvShowBasicElement);


                }

                @Override
                public void onFailure(Call<JsonTvShowBasicInfoRoot> call, Throwable t) {
                    Log.e("BIGFAIL", t.getMessage());
                }
            });


    }

    public void insertTvShowDetailsInfo(int tvShowId) {
        ApiService apiService = ApiBuilder.getRetrofitInstance().create(ApiService.class);
        Call<JsonTvShowDetailsInfoRoot> jsonTvShowDetailsRootCall = apiService.getTvShowDetailed(tvShowId);
        jsonTvShowDetailsRootCall.enqueue(new Callback<JsonTvShowDetailsInfoRoot>() {
            @Override
            public void onResponse(Call<JsonTvShowDetailsInfoRoot> call, Response<JsonTvShowDetailsInfoRoot> response) {
                JsonTvShowDetailsInfoRoot jsonTvShowDetailsInfoRoot = response.body();
                insertTvShowDetailsCallback(jsonTvShowDetailsInfoRoot);
            }

            @Override
            public void onFailure(Call<JsonTvShowDetailsInfoRoot> call, Throwable t) {
                Log.e("BIGFAIL", t.getMessage());
            }
        });

    }

    private void insertTvShowDetailsCallback(JsonTvShowDetailsInfoRoot data) {
        JsonTvShowDetailsInfo tvShowDetails = data.getTvShow();

        int showId = tvShowDetails.getId();
        String description = tvShowDetails.getDescription();
        String youtubeLink = tvShowDetails.getYoutubeLink();
        String rating = tvShowDetails.getRating();


        UpdateTvShowDetailsParams newTvShowDetailsParams = new UpdateTvShowDetailsParams(showId, description, youtubeLink, rating);
        updateTvShowDetails(newTvShowDetailsParams);


//        if(isTvShowPictureExist(showId)){
//            Log.i(TAG, "insertTvShowDetailsCallback: picture");
//        }else {
            int pictureSize = tvShowDetails.getPictures().size();
            for (int i = 0; i < pictureSize; i++) {
                String pictureUrl = tvShowDetails.getPictures().get(i);

                TvShowPicture tvShowPicture = new TvShowPicture(showId, pictureUrl);
                insertTvShowPicture(tvShowPicture);
            }
//        }

//        if(isTvShowEpisodeExist(showId)){
//            Log.i(TAG, "insertTvShowDetailsCallback: episode");
//        }else{
            int episodeSize = tvShowDetails.getJsonTvShowDetailsEpisodes().size();
            for (int i = 0; i < episodeSize; i++) {
                JsonTvShowDetailsEpisode currentEpisode = tvShowDetails.getJsonTvShowDetailsEpisodes().get(i);

                int seasonNum = currentEpisode.getSeason();
                int episodeNum = currentEpisode.getEpisode();
                String episodeName = currentEpisode.getName();
                String episodeAirDate = currentEpisode.getAirDate();

                TvShowEpisode newTvShowEpisode = new TvShowEpisode(showId, seasonNum, episodeNum, episodeName, episodeAirDate);
                insertTvShowEpisode(newTvShowEpisode);
            }
//        }

//        if(isTvShowGenreExist(showId)){
//            Log.i(TAG, "insertTvShowDetailsCallback: genre");
//        }else{
            int genresList = tvShowDetails.getGenres().size();
            for (int i = 0; i < genresList; i++) {

                String genreName = tvShowDetails.getGenres().get(i);
                TvShowGenre tvShowGenre = new TvShowGenre(showId, genreName);
                insertTvShowGenre(tvShowGenre);
            }
//        }

    }

    private void generateTvShowsCallback(JsonTvShowBasicInfoRoot data) {
        for (int i = 0; i < data.getTVShows().size(); i++) {
            JsonTvShowBasicInfo urlTvShow = data.getTVShows().get(i);

            int tvShowId = urlTvShow.getId();
            String tvShowName = urlTvShow.getName();
            String tvShowStatus = urlTvShow.getStatus();
            String tvShowStartDate = urlTvShow.getStartDate();
            String tvShowEndDate = urlTvShow.getEndDate();
            String tvShowCountry = urlTvShow.getCountry();
            String tvShowNetwork = urlTvShow.getNetwork();
            String tvShowImage = urlTvShow.getImageThumbnailPath();


            TvShow tvShow = new TvShow(tvShowId, tvShowName, tvShowStartDate, tvShowEndDate, tvShowCountry, tvShowNetwork, tvShowStatus, tvShowImage);
//            if (isTvShowExistInDb(tvShowId)) {
//                updateTvShow(tvShow);
//            } else {
//                insertTvShow(tvShow);
//            }
//            insertTvShowDetailsInfo(tvShowId);
        }
    }

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
            JsonTvShowBasicInfo jsonTvShow = root.getTvShows().get(i);
            TvShow tvShow = new TvShow(jsonTvShow.getId(), jsonTvShow.getName(), jsonTvShow.getStartDate(), jsonTvShow.getEndDate(), jsonTvShow.getCountry(), jsonTvShow.getNetwork(), jsonTvShow.getStatus(), jsonTvShow.getImageThumbnailPath());
            searchedTvShows.add(tvShow);
        }
        allSearchTvShows.postValue(searchedTvShows);
    }

    public void insertFromSearch(TvShow tvShow) {
        insertOrUpdateTvShow(tvShow);
        insertTvShowDetailsInfo(tvShow.getTvShowId());
    }

    //Help functions
    /*
    private Boolean isTvShowExistInDb(int tvShowId) {
        boolean isExist = false;
        for (int i = 0; i < allTvShows.getValue().size(); i++) {
            TvShow tvShow = allTvShows.getValue().get(i);
            if (tvShowId == tvShow.getTvShowId()) {
                isExist = true;
                return isExist;
            }
        }
        return isExist;
    }
*/
    private Boolean isTvShowPictureExist(int tvShowId) {
        boolean isExist = false;
        if (getTvShowPicturesByShowId(tvShowId).size() != 0) {
            isExist = true;
            return isExist;
        }
        return isExist;
    }

    private Boolean isTvShowEpisodeExist(int tvShowId) {
        boolean isExist = false;
        if (getTvShowEpisodesById(tvShowId).size() != 0) {
            isExist = true;
            return isExist;
        }
        return isExist;
    }

    private Boolean isTvShowGenreExist(int tvShowId) {
        boolean isExist = false;
        if (getTvShowGenresById(tvShowId).size() != 0) {
            isExist = true;
            return isExist;
        }
        return isExist;
    }

    public void clearSearchedTvShows() {
        List<TvShow> emptyTvShows = new ArrayList<>();
        allSearchTvShows.postValue(emptyTvShows);
//        if(allSearchTvShows.getValue() != null){
//            allSearchTvShows.getValue().clear();
//        }
    }

    private LiveData<List<TvShowTest>> watchlist() {
        MutableLiveData<List<TvShowTest>> tvShowFullList = new MutableLiveData<>();
        List<TvShow> allWatchingTvShows = appDao.getWatchlistListTvShows(MainActivity.TVSHOW_WATCHING_FLAG_YES);
        List<TvShowTest> tvShowTestsss = new ArrayList<>();
        if(allWatchingTvShows != null) {
            for (int i = 0; i < allWatchingTvShows.size(); i++) {
                TvShow tvShow = allWatchingTvShows.get(i);
                int id = tvShow.getTvShowId();
                List<TvShowEpisode> episodes = getTvShowEpisodesById(id);
                List<TvShowGenre> genres = getTvShowGenresById(id);
                List<TvShowPicture> pictures =  getTvShowPicturesByShowId(id);

                TvShowTest test = new TvShowTest(tvShow, episodes, genres, pictures);
                tvShowTestsss.add(test);
            }
            tvShowFullList.setValue(tvShowTestsss);
        }
        return tvShowFullList;
    }


    //TvShow

    public void insertTvShows(List<TvShow> tvShows) {
        new InsertTvShowsAsyncTask(appDao).execute(tvShows);
    }


    public void insertOrUpdateTvShow(TvShow tvShow) {
        if(getTvShowById(tvShow.getTvShowId()) != null){
            updateTvShow(tvShow);
        }else{
            insertTvShow(tvShow);
        }
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

    //TvShowFullFragment
    public List<TvShowFull> getTvShowWithPicturesAndEpisodesById(int tvShowId) {
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

    //TvShowFullFragment AsyncTasks
    private static class GetTvShowWithPicturesAndEpisodesByIdAsyncTask extends AsyncTask<Integer, Void, List<TvShowFull>> {
        private AppDao appDao;

        GetTvShowWithPicturesAndEpisodesByIdAsyncTask(AppDao appDao) {
            this.appDao = appDao;
        }

        @Override
        protected List<TvShowFull> doInBackground(Integer... integers) {
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
