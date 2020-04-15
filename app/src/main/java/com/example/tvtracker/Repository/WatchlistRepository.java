package com.example.tvtracker.Repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.tvtracker.WatchlistModel.Watchlist;

import java.util.List;

public class WatchlistRepository {
    private WatchListDao watchListDao;
    private LiveData<List<Watchlist>> allWatchlistTvShows;

    public WatchlistRepository(Application application){
        TvShowDatabase database = TvShowDatabase.getInstance(application);
        watchListDao = database.watchListDao();
        allWatchlistTvShows =  watchListDao.getAllWatchlist();
    }

    public LiveData<List<Watchlist>> getAllWatchlistTvShows() { return  allWatchlistTvShows; }

    public void insertWatchListTvShow(Watchlist watchlist){
        new InsertWatchlistTvShowAsyncTask(watchListDao).execute(watchlist);
    }

    private static class InsertWatchlistTvShowAsyncTask extends AsyncTask<Watchlist, Void, Void> {
        private  WatchListDao watchListDao;
        private InsertWatchlistTvShowAsyncTask(WatchListDao watchListDao) { this.watchListDao = watchListDao;}

        @Override
        protected Void doInBackground(Watchlist... watchlists) {
            watchListDao.insertWatchlistItem(watchlists[0]);
            return null;
        }
    }


}
