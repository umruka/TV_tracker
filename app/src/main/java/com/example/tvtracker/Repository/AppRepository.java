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
import com.example.tvtracker.Models.UpdateTvShowBasicWatchingFlagParams;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AppRepository {
    private AppDao appDao;
    private LiveData<List<TvShowBasic>> allTvShows;
    private LiveData<List<TvShowCombined>> allTvShowsCombined;
    private LiveData<List<TvShowDetails>> allTvShowsDetails;

    public AppRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        appDao = database.appDao();
        allTvShows = appDao.getAllTvShowsBasic();
        allTvShowsDetails = appDao.getAllTvShowsDetails();
        allTvShowsCombined = appDao.getAllTvShowsCombined("yes");
    }

    public LiveData<List<TvShowBasic>> getAllTvShows() {
        return allTvShows;
    }

    public LiveData<List<TvShowDetails>> getAllTvShowsDetails() {
        return allTvShowsDetails;
    }

    public LiveData<List<TvShowCombined>> getAllTvShowsCombined() { return allTvShowsCombined;}

    //Api calls
    public void insertMostPopularTvShowsBasicInfo(){

        ApiService apiService = ApiBuilder.getRetrofitInstance().create(ApiService.class);
        Call<JsonTvShowBasicRoot> jsonTvShowBasicRootCall = apiService.getTvShowsBasic();
        jsonTvShowBasicRootCall.enqueue(new Callback<JsonTvShowBasicRoot>() {
            @Override
            public void onResponse(Call<JsonTvShowBasicRoot> call, Response<JsonTvShowBasicRoot> response) {

                    JsonTvShowBasicRoot jsonTvShowBasicElement = response.body();
                    generateTvShowsCallback(jsonTvShowBasicElement);

            }
            @Override
            public void onFailure(Call<JsonTvShowBasicRoot> call, Throwable t) {
              Log.e("BIGFAIL", t.getMessage());
            }
        });

    }

    public void insertTvShowDetailsInfo(int tvShowId){
        ApiService apiService = ApiBuilder.getRetrofitInstance().create(ApiService.class);
        Call<JsonTvShowDetailsRoot> jsonTvShowDetailsRootCall = apiService.getTvShowDetailed(tvShowId);
        jsonTvShowDetailsRootCall.enqueue(new Callback<JsonTvShowDetailsRoot>() {
            @Override
            public void onResponse(Call<JsonTvShowDetailsRoot> call, Response<JsonTvShowDetailsRoot> response) {
                JsonTvShowDetailsRoot jsonTvShowDetailsRoot = response.body();
                insertTvShowDetailsCallback(jsonTvShowDetailsRoot);
            }

            @Override
            public void onFailure(Call<JsonTvShowDetailsRoot> call, Throwable t) {
                Log.e("BIGFAIL", t.getMessage());
            }
        });
    }

    private void insertTvShowDetailsCallback(JsonTvShowDetailsRoot data){
        JsonTvShowDetails tvShowFull = data.getTvShow();

        int showId = tvShowFull.getId();
        String description = tvShowFull.getDescription();
        String youtubeLink = tvShowFull.getYoutubeLink();
        String rating = tvShowFull.getRating();
        String imagePath = tvShowFull.getImagePath();

        TvShowDetails newTvShowDetails = new TvShowDetails(showId, description, youtubeLink, rating, imagePath);
        insertTvShowDetails(newTvShowDetails);
    }

    private void generateTvShowsCallback(JsonTvShowBasicRoot data) {
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
                updateTvShowBasic(tvShowBasic);
            } else {
                insertTvShowBasic(tvShowBasic);
            }
        }
    }

    //Help functions
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



    //TvShowBasic

    public TvShowBasic getTvShowBasicById(int Id) {
        try {
            return new GetTvShowBasicAsyncTask(appDao).execute(Id).get();
        } catch (Exception e) {
            Log.e("Error:", e.getMessage());
        }
        return null;
    }

    public void insertTvShowBasic(TvShowBasic tvShowBasic) {

        new InsertTvShowBasicAsyncTask(appDao).execute(tvShowBasic);
    }

    public void updateTvShowBasic(TvShowBasic tvShowBasic) {
        new UpdateTvShowBasicAsyncTask(appDao).execute(tvShowBasic);
    }

    public void updateTvShowBasicWatchingFlag(UpdateTvShowBasicWatchingFlagParams params) {
        new UpdateTvShowBasicWatchingFlagAsyncTask(appDao).execute(params);
    }

    public void deleteTvShowBasic(int id) {
        new DeleteTvShowBasicAsyncTask(appDao).execute(id);
    }


    public void deleteAllTvShowsBasic() {
        new DeleteAllTvShowsBasic(appDao).execute();
    }

    //TvShowDetails
    public TvShowDetails getTvShowFullById(int Id) {
        try {
            return new GetTvShowDetailsAsyncTask(appDao).execute(Id).get();
        } catch (Exception e) {
            Log.e("Error:", e.getMessage());
        }
        return null;
    }

    public void insertTvShowDetails(TvShowDetails tvShowDetails) {
        new InsertTvShowDetailsAsyncTask(appDao).execute(tvShowDetails);
    }

    public void updateTvShowDetails(TvShowDetails tvShowDetails) {
        new UpdateTvShowDetailsAsyncTask(appDao).execute(tvShowDetails);
    }

    public void deleteTvShowDetails(int id) {
        new DeleteTvShowDetailsAsyncTask(appDao).execute(id);
    }

    public void deleteAllTvShowsDetail() {
        new DeleteTvShowDetailsAsyncTask(appDao).execute();
    }

    //TvShowBasic AsyncTasks
    private static class GetTvShowBasicAsyncTask extends AsyncTask<Integer, Void, TvShowBasic> {
        private AppDao appDao;

        private GetTvShowBasicAsyncTask(AppDao appDao) {
            this.appDao = appDao;
        }

        @Override
        protected TvShowBasic doInBackground(Integer... integers) {
            return appDao.getTvShowBasicById(integers[0]);
        }

        @Override
        protected void onPostExecute(TvShowBasic tvShowBasic) {
            super.onPostExecute(tvShowBasic);
        }
    }

    private static class InsertTvShowBasicAsyncTask extends AsyncTask<TvShowBasic, Void, Void> {
        private AppDao appDao;

        private InsertTvShowBasicAsyncTask(AppDao appDao) {
            this.appDao = appDao;
        }

        @Override
        protected Void doInBackground(TvShowBasic... tvShowBasics) {
            appDao.insertTvShowBasic(tvShowBasics[0]);
            return null;
        }
    }


    private static class UpdateTvShowBasicAsyncTask extends AsyncTask<TvShowBasic, Void, Void> {
        private AppDao appDao;

        private UpdateTvShowBasicAsyncTask(AppDao appDao) {
            this.appDao = appDao;
        }

        @Override
        protected Void doInBackground(TvShowBasic... tvShowBasics) {
            TvShowBasic tvShowBasic = tvShowBasics[0];
            appDao.updateTvShowBasic(tvShowBasic.getTvShowId(), tvShowBasic.getTvShowName(), tvShowBasic.getTvShowStatus());
            return null;
        }
    }

    private static class UpdateTvShowBasicWatchingFlagAsyncTask extends AsyncTask<UpdateTvShowBasicWatchingFlagParams, Void, Void> {
        private AppDao appDao;

        private UpdateTvShowBasicWatchingFlagAsyncTask(AppDao appDao) {
            this.appDao = appDao;
        }

        @Override
        protected Void doInBackground(UpdateTvShowBasicWatchingFlagParams... params) {
            int id = params[0].getId();
            String flag = params[0].getFlag();
            appDao.updateTvShowBasicWatchingFlag(id, flag);
            return null;
        }
    }

    private static class DeleteTvShowBasicAsyncTask extends AsyncTask<Integer, Void, Void> {
        private AppDao appDao;

        private DeleteTvShowBasicAsyncTask(AppDao appDao) {
            this.appDao = appDao;
        }

        @Override
        protected Void doInBackground(Integer... integers) {
            appDao.deleteTvShowBasicById(integers[0]);
            return null;
        }
    }

    private static class DeleteAllTvShowsBasic extends AsyncTask<Void, Void, Void> {
        private AppDao appDao;

        private DeleteAllTvShowsBasic(AppDao appDao) {
            this.appDao = appDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            appDao.deleteAllTvShowsBasic();
            return null;
        }
    }

    //TvShowDetails AsyncTasks
    private static class GetTvShowDetailsAsyncTask extends AsyncTask<Integer, Void, TvShowDetails> {
        private AppDao appDao;

        private GetTvShowDetailsAsyncTask(AppDao appDao) {
            this.appDao = appDao;
        }

        @Override
        protected TvShowDetails doInBackground(Integer... integers) {
            return appDao.getTvShowDetailsById(integers[0]);
        }

        @Override
        protected void onPostExecute(TvShowDetails tvShowDetails) {
            super.onPostExecute(tvShowDetails);
        }
    }

    private static class InsertTvShowDetailsAsyncTask extends AsyncTask<TvShowDetails, Void, Void> {
        private AppDao appDao;

        private InsertTvShowDetailsAsyncTask(AppDao appDao) {
            this.appDao = appDao;
        }

        @Override
        protected Void doInBackground(TvShowDetails... tvShowDetails) {
            appDao.insertTvShowDetails(tvShowDetails[0]);
            return null;
        }
    }

    private static class UpdateTvShowDetailsAsyncTask extends AsyncTask<TvShowDetails, Void, Void> {
        private AppDao appDao;

        private UpdateTvShowDetailsAsyncTask(AppDao appDao) {
            this.appDao = appDao;
        }

        @Override
        protected Void doInBackground(TvShowDetails... tvShowDetails) {
            TvShowDetails tvShowDetail = tvShowDetails[0];
            appDao.updateTvShowDetails(tvShowDetail.getTvShowDesc(), tvShowDetail.getTvShowYoutubeLink(), tvShowDetail.getTvShowRating(), tvShowDetail.getTvShowImagePath());
            return null;
        }
    }

    private static class DeleteTvShowDetailsAsyncTask extends AsyncTask<Integer, Void, Void> {
        private AppDao appDao;

        private DeleteTvShowDetailsAsyncTask(AppDao appDao) {
            this.appDao = appDao;
        }

        @Override
        protected Void doInBackground(Integer... integers) {
            appDao.deleteTvShowDetailsById(integers[0]);
            return null;
        }
    }

    private static class DeleteAllTvShowsDetails extends AsyncTask<Void, Void, Void> {
        private AppDao appDao;

        private DeleteAllTvShowsDetails(AppDao appDao) {
            this.appDao = appDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            appDao.deleteAllTvShowsDetails();
            return null;
        }
    }

}
