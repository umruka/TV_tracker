package com.example.tvtracker.WatchlistModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.tvtracker.Repository.AppRepository;
import com.example.tvtracker.Models.TvShowCombined;

import java.util.List;

public class WatchlistViewModel extends AndroidViewModel {
    private AppRepository repository;
    private LiveData<List<TvShowCombined>> allTVShowsWatchingCombined;

    public WatchlistViewModel(@NonNull Application application) {
        super(application);
        repository = new AppRepository(application);
        allTVShowsWatchingCombined = repository.getAllTvShowsCombined();
    }

    public LiveData<List<TvShowCombined>> getAllTVShowsWatchingCombined() {
        return allTVShowsWatchingCombined;
    }

}
