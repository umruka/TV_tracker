package com.example.tvtracker.UI.Watchlist;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;

import com.example.tvtracker.DTO.Models.QueryModels.TvShowFull;
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
            public void onChanged(List<TvShowFull> tvShowFulls) {
                watchlistListObservable.setValue(tvShowFulls);
            }
        });

    }

    public MediatorLiveData<List<TvShowFull>> getWatchlistListObservable() {
        return watchlistListObservable;
    }

    public void fetchWatchlistData() {
        repository.fetchWatchlist();
    }
}
