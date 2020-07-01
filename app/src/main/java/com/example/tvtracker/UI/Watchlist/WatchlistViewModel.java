package com.example.tvtracker.UI.Watchlist;

import android.app.Application;
import android.util.Pair;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;

import com.example.tvtracker.Models.TvShowFull;
import com.example.tvtracker.Repository.AppRepository;

import java.util.List;

public class WatchlistViewModel extends AndroidViewModel {

    private AppRepository repository;
    private MediatorLiveData<List<TvShowFull>> watchlistListObservable = new MediatorLiveData<>();

    public WatchlistViewModel(@NonNull Application application) {
        super(application);
        repository = new AppRepository(application);
        watchlistListObservable.addSource(repository.getWatchlistListObservable(), new Observer<List<TvShowFull>>() {
            @Override
            public void onChanged(List<TvShowFull> TvShowFulls) {
                watchlistListObservable.setValue(TvShowFulls);
            }
        });

    }

    LiveData<List<TvShowFull>> getWatchlistListObservable() {
        return watchlistListObservable;
    }

    void fetchWatchlistData() {
        repository.fetchWatchlist();
    }

    void changeEpisodeWatchedFlag(Pair<Integer, Boolean> params) {
        repository.setTvShowEpisodeIsWatchedFlag(params);
    }


}
