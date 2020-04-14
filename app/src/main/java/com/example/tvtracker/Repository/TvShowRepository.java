package com.example.tvtracker.Repository;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.tvtracker.TvShowModel.TvShow;

import java.util.List;

public class TvShowRepository {
    private TvShowDao tvShowDao;
    private LiveData<List<TvShow>> allTvShows;

    public TvShowRepository(Application application){
        TvShowDatabase database = TvShowDatabase.getInstance(application);
        tvShowDao = database.tv_showDao();
        allTvShows = tvShowDao.getAllTvShows();
    }

    public LiveData<List<TvShow>> getAllTvShows() { return allTvShows; }

    public void insertTvShow(TvShow tvShow){
        new InsertTvShowAsyncTask(tvShowDao).execute(tvShow);
    }

    public TvShow getTvShowById(int Id){
        try {
            return new GetTvShowAsyncTask(tvShowDao).execute(Id).get();
        }catch (Exception e){
            Log.e("Error:", e.getMessage());
        }
        return null;
    }

    public void deleteAllTvShows() { new DeleteAllTvShows(tvShowDao).execute(); }

    public void updateTvShow(TvShow tvShow){
        new UpdateTvShowAsyncTask(tvShowDao).execute(tvShow);
    }

    private static class  InsertTvShowAsyncTask extends AsyncTask<TvShow, Void, Void> {
        private TvShowDao tvShowDao;
        private InsertTvShowAsyncTask(TvShowDao tvShowDao) { this.tvShowDao = tvShowDao;}

        @Override
        protected Void doInBackground(TvShow... tvShows) {
            tvShowDao.insertTVShow(tvShows[0]);
            return null;
        }
    }

    private static class DeleteAllTvShows extends AsyncTask<Void, Void, Void> {
        private TvShowDao tvShowDao;
        private DeleteAllTvShows(TvShowDao tvShowDao) { this.tvShowDao = tvShowDao;}

        @Override
        protected Void doInBackground(Void... voids) {
            tvShowDao.deleteAllTvShows();
            return null;
        }
    }

    private static class UpdateTvShowAsyncTask extends AsyncTask<TvShow, Void, Void> {
        private TvShowDao tvShowDao;
        private UpdateTvShowAsyncTask(TvShowDao tvShowDao) { this.tvShowDao = tvShowDao;}

        @Override
        protected Void doInBackground(TvShow... tvShows) {
            TvShow tvShow = tvShows[0];
            tvShowDao.updateTvShow(tvShow.getTvShowId(), tvShow.getTvShowName(), tvShow.getTvShowStatus());
            return null;
        }
    }

    private  static class GetTvShowAsyncTask extends AsyncTask<Integer, Void, TvShow> {
        private TvShowDao tvShowDao;
        private GetTvShowAsyncTask(TvShowDao tvShowDao) { this.tvShowDao = tvShowDao;}

        @Override
        protected TvShow doInBackground(Integer... integers) {
            return tvShowDao.getTvShowById(integers[0]);
        }

        @Override
        protected void onPostExecute(TvShow tvShow) {
            super.onPostExecute(tvShow);
        }
    }



}
