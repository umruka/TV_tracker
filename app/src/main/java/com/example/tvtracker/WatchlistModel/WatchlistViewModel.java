package com.example.tvtracker.WatchlistModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.tvtracker.Repository.WatchlistRepository;

import java.util.List;

public class WatchlistViewModel extends AndroidViewModel {
    private WatchlistRepository repository;
    private LiveData<List<Watchlist>> allWatchlist;

    public WatchlistViewModel(@NonNull Application application){
        super(application);
        repository = new WatchlistRepository(application);
        allWatchlist = repository.getAllWatchlistTvShows();
    }
    public LiveData<List<Watchlist>> getAllWatchlist() {
        return allWatchlist;
    }

    public void insertWatchlistItem(Watchlist watchlist) { repository.insertWatchListTvShow(watchlist);}


}
