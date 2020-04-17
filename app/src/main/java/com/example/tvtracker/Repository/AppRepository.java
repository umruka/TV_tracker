package com.example.tvtracker.Repository;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.tvtracker.Models.TvShowCombined;
import com.example.tvtracker.TvShowFullModel.TvShowFull;
import com.example.tvtracker.TvShowModel.TvShow;
import com.example.tvtracker.Models.UpdateTvShowWatchingFlagParams;

import java.util.List;

public class AppRepository {
    private AppDao appDao;
    private LiveData<List<TvShow>> allTvShows;
    private LiveData<List<TvShowCombined>> allTvShowsCombined;
    private LiveData<List<TvShowFull>> allTvShowsFull;

    public AppRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        appDao = database.appDao();
        allTvShows = appDao.getAllTvShows();
        allTvShowsFull = appDao.getAllTvShowsFull();
        allTvShowsCombined = appDao.getAllTvShowsCombined("yes");
    }

    public LiveData<List<TvShow>> getAllTvShows() {
        return allTvShows;
    }

    public LiveData<List<TvShowFull>> getAllTvShowsFull() {
        return allTvShowsFull;
    }

    public LiveData<List<TvShowCombined>> getAllTvShowsCombined() { return allTvShowsCombined;}

    public void insertTvShowFull(TvShowFull tvShowFull) {
    new InsertTvShowFullAsyncTask(appDao).execute(tvShowFull);
    }

    public void insertTvShow(TvShow tvShow) {
        new InsertTvShowAsyncTask(appDao).execute(tvShow);
    }

    public void deleteTvShow(int id) {
        new DeleteTvShowAsyncTask(appDao).execute(id);
    }

    public TvShow getTvShowById(int Id) {
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

    public void updateTvShow(TvShow tvShow) {
        new UpdateTvShowAsyncTask(appDao).execute(tvShow);
    }

    public void updateTvShowWatchingFlag(UpdateTvShowWatchingFlagParams params) {
        new UpdateTvShowWatchingFlagAsyncTask(appDao).execute(params);
    }



    public void insertTvShowDetailed(TvShowFull tvShowFull) {
        new InsertTvShowDetailedAsyncTask(appDao).execute(tvShowFull);
    }

    public void deleteTvShowFull(int id) {
        new DeleteTvShowDetailedAsyncTask(appDao).execute(id);
    }

    public TvShowFull getTvShowFullById(int Id) {
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

    public void updateTvShowFull(TvShowFull tvShowFull) {
        new UpdateTvShowDetailedAsyncTask(appDao).execute(tvShowFull);
    }

    private static class InsertTvShowAsyncTask extends AsyncTask<TvShow, Void, Void> {
        private AppDao appDao;

        private InsertTvShowAsyncTask(AppDao appDao) {
            this.appDao = appDao;
        }

        @Override
        protected Void doInBackground(TvShow... tvShows) {
            appDao.insertTVShow(tvShows[0]);
            return null;
        }
    }

    private static class InsertTvShowFullAsyncTask extends AsyncTask<TvShowFull, Void, Void> {
        private AppDao appDao;

        private InsertTvShowFullAsyncTask(AppDao appDao) {
            this.appDao = appDao;
        }

        @Override
        protected Void doInBackground(TvShowFull... tvShowFulls) {
            appDao.insertTvShowFull(tvShowFulls[0]);
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

    private static class InsertTvShowDetailedAsyncTask extends AsyncTask<TvShowFull, Void, Void> {
        private AppDao appDao;

        private InsertTvShowDetailedAsyncTask(AppDao appDao) {
            this.appDao = appDao;
        }

        @Override
        protected Void doInBackground(TvShowFull... tvShowDetails) {
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



    private static class UpdateTvShowDetailedAsyncTask extends AsyncTask<TvShowFull, Void, Void> {
        private AppDao appDao;

        private UpdateTvShowDetailedAsyncTask(AppDao appDao) {
            this.appDao = appDao;
        }

        @Override
        protected Void doInBackground(TvShowFull... tvShowDetails) {
            TvShowFull tvShowFull = tvShowDetails[0];

            return null;
        }
    }

    private static class GetTvShowDetailedAsyncTask extends AsyncTask<Integer, Void, TvShowFull> {
        private AppDao appDao;

        private GetTvShowDetailedAsyncTask(AppDao appDao) {
            this.appDao = appDao;
        }

        @Override
        protected TvShowFull doInBackground(Integer... integers) {
            return appDao.getTvShowDetailedById(integers[0]);
        }

        @Override
        protected void onPostExecute(TvShowFull tvShowFull) {
            super.onPostExecute(tvShowFull);
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
