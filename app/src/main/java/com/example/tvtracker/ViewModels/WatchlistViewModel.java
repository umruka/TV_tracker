package com.example.tvtracker.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;

import com.example.tvtracker.Models.Basic.Resource;
import com.example.tvtracker.Models.QueryModels.TvShowTest;
import com.example.tvtracker.Models.TvShow;
import com.example.tvtracker.Repository.AppRepository;

import java.util.List;

public class WatchlistViewModel extends AndroidViewModel {

    private AppRepository repository;
    private MediatorLiveData<Resource<List<TvShowTest>>> watchlistListObservable = new MediatorLiveData<>();

    public WatchlistViewModel(@NonNull Application application) {
        super(application);
        repository = new AppRepository(application);
        watchlistListObservable.addSource(repository.getWatchlistListObservable(), new Observer<Resource<List<TvShowTest>>>() {
            @Override
            public void onChanged(Resource<List<TvShowTest>> tvShows) {
                watchlistListObservable.setValue(tvShows);
            }
        });
    }

    public LiveData<Resource<List<TvShowTest>>> getWatchlistListObservable() {
        return watchlistListObservable;
    }

    public void fetchData() {
        repository.fetchWatchlist();
    }

}
