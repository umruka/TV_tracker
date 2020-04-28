package com.example.tvtracker.Repository;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.tvtracker.Api.ApiBuilder;
import com.example.tvtracker.Api.ApiService;
import com.example.tvtracker.JsonModels.JsonTvShowSearchRoot;
import com.example.tvtracker.JsonModels.TvShowBasicInfo.JsonTvShowBasicInfo;
import com.example.tvtracker.JsonModels.TvShowBasicInfo.JsonTvShowBasicInfoRoot;
import com.example.tvtracker.JsonModels.TvShowDetails.JsonTvShowDetailsEpisode;
import com.example.tvtracker.JsonModels.TvShowDetails.JsonTvShowDetailsInfo;
import com.example.tvtracker.JsonModels.TvShowDetails.JsonTvShowDetailsInfoRoot;
import com.example.tvtracker.Models.TvShow;
import com.example.tvtracker.Models.TvShowEpisode;
import com.example.tvtracker.Models.TvShowPicture;
import com.example.tvtracker.Models.QueryModels.TvShowWithPicturesAndEpisodes;
import com.example.tvtracker.Models.Params.UpdateTvShowDetailsParams;
import com.example.tvtracker.Models.Params.UpdateTvShowWatchingFlagParams;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AppRepository {
    private AppDao appDao;
    private LiveData<List<TvShow>> allTvShows;
    private LiveData<List<TvShow>> allWatchingTvShows;

    public AppRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        appDao = database.appDao();
        allTvShows = appDao.getAllTvShows();
        allWatchingTvShows = appDao.getWatchlistTvShows("yes");
    }

    public LiveData<List<TvShow>> getAllTvShows() {
        return allTvShows;
    }

    public LiveData<List<TvShow>> getAllWatchingTvShows() {
        return allWatchingTvShows;
    }


    //Api calls
    public void insertMostPopularTvShowsBasicInfo() {

        ApiService apiService = ApiBuilder.getRetrofitInstance().create(ApiService.class);
        Call<JsonTvShowBasicInfoRoot> jsonTvShowBasicRootCall = apiService.getTvShowsBasic();
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

        int pictureSize = tvShowDetails.getPictures().size();
        for (int i = 0; i < pictureSize; i++) {
            String pictureUrl = tvShowDetails.getPictures().get(i);

            TvShowPicture tvShowPicture = new TvShowPicture(showId, pictureUrl);
            insertTvShowPicture(tvShowPicture);
        }

        int episodeSize = tvShowDetails.getJsonTvShowDetailsEpisodes().size();
        for(int i=0; i< episodeSize; i++){
            JsonTvShowDetailsEpisode currentEpisode = tvShowDetails.getJsonTvShowDetailsEpisodes().get(i);

            int seasonNum  = currentEpisode.getSeason();
            int episodeNum = currentEpisode.getEpisode();
            String episodeName = currentEpisode.getName();
            String episodeAirDate = currentEpisode.getAirDate();

            TvShowEpisode newTvShowEpisode = new TvShowEpisode(showId, seasonNum, episodeNum, episodeName, episodeAirDate);
            insertTvShowEpisode(newTvShowEpisode);
        }


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
            if (isTvShowExistInDb(tvShowId)) {
                updateTvShow(tvShow);
            } else {
                insertTvShow(tvShow);
            }
        }
    }

    public void searchTvShow(String searchWord, int pageNum) {
        ApiService apiService = ApiBuilder.getRetrofitInstance().create(ApiService.class);
        Call<JsonTvShowSearchRoot> jsonTvShowSearchRootCall = apiService.getTvShowSearch(searchWord, pageNum);
        jsonTvShowSearchRootCall.enqueue(new Callback<JsonTvShowSearchRoot>() {
            @Override
            public void onResponse(Call<JsonTvShowSearchRoot> call, Response<JsonTvShowSearchRoot> response) {
                Log.e("BIGFAIL", String.valueOf(response));
            }

            @Override
            public void onFailure(Call<JsonTvShowSearchRoot> call, Throwable t) {
                Log.e("BIGFAIL", t.getMessage());
            }
        });
    }


    //Help functions
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

    //TvShow


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

    public void updateTvShowWatchingFlag(UpdateTvShowWatchingFlagParams params) {
        new UpdateTvShowWatchingFlagAsyncTask(appDao).execute(params);
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



    //TvShowWithPicturesAndEpisodes
    public List<TvShowWithPicturesAndEpisodes> getTvShowWithPicturesAndEpisodesById(int tvShowId) {
        try {
            return new GetTvShowWithPicturesAndEpisodesByIdAsyncTask(appDao).execute(tvShowId).get();
        } catch (Exception e) {
            Log.e("Error:", e.getMessage());
        }
        return null;
    }

    //TvShow AsyncTasks
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

    //TvShowWithPicturesAndEpisodes AsyncTasks
    private static class GetTvShowWithPicturesAndEpisodesByIdAsyncTask extends AsyncTask<Integer, Void, List<TvShowWithPicturesAndEpisodes>> {
        private AppDao appDao;

        GetTvShowWithPicturesAndEpisodesByIdAsyncTask(AppDao appDao) {
            this.appDao = appDao;
        }

        @Override
        protected List<TvShowWithPicturesAndEpisodes> doInBackground(Integer... integers) {
            return appDao.getTvShowWithPicturesAndEpisodesById(integers[0]);
        }
    }


}
