package com.example.tvtracker.Repository;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.tvtracker.Api.ApiBuilder;
import com.example.tvtracker.Api.ApiService;
import com.example.tvtracker.JsonModels.TvShowBasic.JsonTvShowBasic;
import com.example.tvtracker.JsonModels.TvShowBasic.JsonTvShowBasicRoot;
import com.example.tvtracker.JsonModels.TvShowDetails.JsonTvShowDetails;
import com.example.tvtracker.JsonModels.TvShowDetails.JsonTvShowDetailsRoot;
import com.example.tvtracker.Models.TvShowCombined;
import com.example.tvtracker.Models.TvShowDetails;
import com.example.tvtracker.Models.TvShowBasic;
import com.example.tvtracker.Models.UpdateTvShowWatchingFlagParams;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AppRepository {
    private AppDao appDao;
    private LiveData<List<TvShowBasic>> allTvShows;
    private LiveData<List<TvShowCombined>> allTvShowsCombined;
    private LiveData<List<TvShowDetails>> allTvShowsFull;

    public AppRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        appDao = database.appDao();
        allTvShows = appDao.getAllTvShows();
        allTvShowsFull = appDao.getAllTvShowsFull();
        allTvShowsCombined = appDao.getAllTvShowsCombined("yes");
    }

    public void getMostPopularTvShowsBasicInfo(){

        ApiService apiService = ApiBuilder.getRetrofitInstance().create(ApiService.class);
        Call<JsonTvShowBasicRoot> jsonTvShowBasicRootCall = apiService.getTvShowsBasic();
        jsonTvShowBasicRootCall.enqueue(new Callback<JsonTvShowBasicRoot>() {
            @Override
            public void onResponse(Call<JsonTvShowBasicRoot> call, Response<JsonTvShowBasicRoot> response) {

                    JsonTvShowBasicRoot jsonTvShowBasicElement = response.body();
                    generateTvShows(jsonTvShowBasicElement);

            }
            @Override
            public void onFailure(Call<JsonTvShowBasicRoot> call, Throwable t) {
              Log.e("BIGFAIL", t.getMessage());
            }
        });

    }

    public void getTvShowDetailInfo(int tvShowId){
        ApiService apiService = ApiBuilder.getRetrofitInstance().create(ApiService.class);
        Call<JsonTvShowDetailsRoot> jsonTvShowDetailsRootCall = apiService.getTvShowDetailed(tvShowId);
        jsonTvShowDetailsRootCall.enqueue(new Callback<JsonTvShowDetailsRoot>() {
            @Override
            public void onResponse(Call<JsonTvShowDetailsRoot> call, Response<JsonTvShowDetailsRoot> response) {
                JsonTvShowDetailsRoot jsonTvShowDetailsRoot = response.body();
                insertTvShowDetails(jsonTvShowDetailsRoot);
            }

            @Override
            public void onFailure(Call<JsonTvShowDetailsRoot> call, Throwable t) {
                Log.e("BIGFAIL", t.getMessage());
            }
        });
    }

    public void insertTvShowDetails(JsonTvShowDetailsRoot data){
        JsonTvShowDetails tvShowFull = data.getTvShow();

        int showId = tvShowFull.getId();
        String description = tvShowFull.getDescription();
        String youtubeLink = tvShowFull.getYoutubeLink();
        String rating = tvShowFull.getRating();
        String imagePath = tvShowFull.getImagePath();

        TvShowDetails newTvShowDetails = new TvShowDetails(showId, description, youtubeLink, rating, imagePath);
        insertTvShowDetailed(newTvShowDetails);
    }


    private Boolean isTvShowExistInDb(int tvShowId) {
        boolean isExist = false;
        for (int i = 0; i < allTvShows.getValue().size(); i++) {
            TvShowBasic tvShowBasic = allTvShows.getValue().get(i);
            if (tvShowId == tvShowBasic.getTvShowId()) {
                isExist = true;
                return isExist;
            }
        }
        return isExist;
    }

    public void generateTvShows(JsonTvShowBasicRoot data) {
        for (int i = 0; i < data.getTVShows().size(); i++) {
            JsonTvShowBasic urlTvShow = data.getTVShows().get(i);

            int tvShowId = urlTvShow.getId();
            String tvShowName = urlTvShow.getName();
            String tvShowStatus = urlTvShow.getStatus();
            String tvShowStartDate = urlTvShow.getStartDate();
            String tvShowEndDate = urlTvShow.getEndDate();
            String tvShowCountry = urlTvShow.getCountry();
            String tvShowNetwork = urlTvShow.getNetwork();
            String tvShowImage = urlTvShow.getImageThumbnailPath();


            TvShowBasic tvShowBasic = new TvShowBasic(tvShowId, tvShowName, tvShowStartDate, tvShowEndDate, tvShowCountry, tvShowNetwork, tvShowStatus, tvShowImage);
            if (isTvShowExistInDb(tvShowId)) {
                updateTvShow(tvShowBasic);
            } else {
                insertTvShow(tvShowBasic);


            }
        }
    }




    public LiveData<List<TvShowBasic>> getAllTvShows() {
        return allTvShows;
    }

    public LiveData<List<TvShowDetails>> getAllTvShowsFull() {
        return allTvShowsFull;
    }

    public LiveData<List<TvShowCombined>> getAllTvShowsCombined() { return allTvShowsCombined;}

    public void insertTvShowFull(final TvShowDetails tvShowDetails) {
    new InsertTvShowFullAsyncTask(appDao).execute(tvShowDetails);
    }

    public void insertTvShow(TvShowBasic tvShowBasic) {

        new InsertTvShowAsyncTask(appDao).execute(tvShowBasic);
    }

    public void deleteTvShow(int id) {
        new DeleteTvShowAsyncTask(appDao).execute(id);
    }

    public TvShowBasic getTvShowById(int Id) {
        try {
            return new GetTvShowAsyncTask(appDao).execute(Id).get();
        } catch (Exception e) {
            Log.e("Error:", e.getMessage());
        }
        return null;
    }

    public void deleteAllTvShows() {
        new DeleteAllTvShows(appDao).execute();
    }

    public void updateTvShow(TvShowBasic tvShowBasic) {
        new UpdateTvShowAsyncTask(appDao).execute(tvShowBasic);
    }

    public void updateTvShowWatchingFlag(UpdateTvShowWatchingFlagParams params) {
        new UpdateTvShowWatchingFlagAsyncTask(appDao).execute(params);
    }



    public void insertTvShowDetailed(TvShowDetails tvShowDetails) {
        new InsertTvShowDetailedAsyncTask(appDao).execute(tvShowDetails);
    }

    public void deleteTvShowFull(int id) {
        new DeleteTvShowDetailedAsyncTask(appDao).execute(id);
    }

    public TvShowDetails getTvShowFullById(int Id) {
        try {
            return new GetTvShowDetailedAsyncTask(appDao).execute(Id).get();
        } catch (Exception e) {
            Log.e("Error:", e.getMessage());
        }
        return null;
    }

    public void deleteAllTvShowsFull() {
        new DeleteTvShowDetailedAsyncTask(appDao).execute();
    }

    public void updateTvShowFull(TvShowDetails tvShowDetails) {
        new UpdateTvShowDetailedAsyncTask(appDao).execute(tvShowDetails);
    }

    private static class InsertTvShowAsyncTask extends AsyncTask<TvShowBasic, Void, Void> {
        private AppDao appDao;

        private InsertTvShowAsyncTask(AppDao appDao) {
            this.appDao = appDao;
        }

        @Override
        protected Void doInBackground(TvShowBasic... tvShowBasics) {
            appDao.insertTVShow(tvShowBasics[0]);
            return null;
        }
    }

    private static class InsertTvShowFullAsyncTask extends AsyncTask<TvShowDetails, Void, Void> {
        private AppDao appDao;

        private InsertTvShowFullAsyncTask(AppDao appDao) {
            this.appDao = appDao;
        }

        @Override
        protected Void doInBackground(TvShowDetails... tvShowDetails) {
            appDao.insertTvShowFull(tvShowDetails[0]);
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

    private static class UpdateTvShowAsyncTask extends AsyncTask<TvShowBasic, Void, Void> {
        private AppDao appDao;

        private UpdateTvShowAsyncTask(AppDao appDao) {
            this.appDao = appDao;
        }

        @Override
        protected Void doInBackground(TvShowBasic... tvShowBasics) {
            TvShowBasic tvShowBasic = tvShowBasics[0];
            appDao.updateTvShow(tvShowBasic.getTvShowId(), tvShowBasic.getTvShowName(), tvShowBasic.getTvShowStatus());
            return null;
        }
    }

    private static class GetTvShowAsyncTask extends AsyncTask<Integer, Void, TvShowBasic> {
        private AppDao appDao;

        private GetTvShowAsyncTask(AppDao appDao) {
            this.appDao = appDao;
        }

        @Override
        protected TvShowBasic doInBackground(Integer... integers) {
            return appDao.getTvShowById(integers[0]);
        }

        @Override
        protected void onPostExecute(TvShowBasic tvShowBasic) {
            super.onPostExecute(tvShowBasic);
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

    private static class InsertTvShowDetailedAsyncTask extends AsyncTask<TvShowDetails, Void, Void> {
        private AppDao appDao;

        private InsertTvShowDetailedAsyncTask(AppDao appDao) {
            this.appDao = appDao;
        }

        @Override
        protected Void doInBackground(TvShowDetails... tvShowDetails) {
            appDao.insertTVShowDetailed(tvShowDetails[0]);
            return null;
        }
    }

    private static class DeleteAllTvShowsDetailed extends AsyncTask<Void, Void, Void> {
        private AppDao appDao;

        private DeleteAllTvShowsDetailed(AppDao appDao) {
            this.appDao = appDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            appDao.deleteAllTvShowsDetailed();
            return null;
        }
    }



    private static class UpdateTvShowDetailedAsyncTask extends AsyncTask<TvShowDetails, Void, Void> {
        private AppDao appDao;

        private UpdateTvShowDetailedAsyncTask(AppDao appDao) {
            this.appDao = appDao;
        }

        @Override
        protected Void doInBackground(TvShowDetails... tvShowDetails) {
            TvShowDetails tvShowFull = tvShowDetails[0];

            return null;
        }
    }

    private static class GetTvShowDetailedAsyncTask extends AsyncTask<Integer, Void, TvShowDetails> {
        private AppDao appDao;

        private GetTvShowDetailedAsyncTask(AppDao appDao) {
            this.appDao = appDao;
        }

        @Override
        protected TvShowDetails doInBackground(Integer... integers) {
            return appDao.getTvShowDetailedById(integers[0]);
        }

        @Override
        protected void onPostExecute(TvShowDetails tvShowDetails) {
            super.onPostExecute(tvShowDetails);
        }
    }

    private static class DeleteTvShowDetailedAsyncTask extends AsyncTask<Integer, Void, Void> {
        private AppDao appDao;

        private DeleteTvShowDetailedAsyncTask(AppDao appDao) {
            this.appDao = appDao;
        }

        @Override
        protected Void doInBackground(Integer... integers) {
            appDao.deleteTvShowDetailedById(integers[0]);
            return null;
        }
    }

}
