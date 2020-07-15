package com.watermelon.UI.Watchlist;

import android.app.Application;
import android.util.Pair;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.watermelon.Models.TvSeriesFull;
import com.watermelon.Repository.AppRepository;

import java.util.List;

public class WatchlistViewModel extends AndroidViewModel {

    private AppRepository repository;
    private LiveData<List<TvSeriesFull>> watchlistListObservable;

    public WatchlistViewModel(@NonNull Application application) {
        super(application);
        repository = new AppRepository(application);
        watchlistListObservable = repository.getWatchlistListObservable();

    }

    LiveData<List<TvSeriesFull>> getWatchlistListObservable() {
        return watchlistListObservable;
    }

    void changeEpisodeWatchedFlag(Pair<Integer, Boolean> params) {
        repository.setTvSeriesEpisodeWatchedFlag(params);
    }


}
