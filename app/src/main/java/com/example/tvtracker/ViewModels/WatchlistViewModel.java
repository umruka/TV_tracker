package com.example.tvtracker.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;

import com.example.tvtracker.Models.Basic.Resource;
import com.example.tvtracker.Models.TvShow;
import com.example.tvtracker.Repository.AppRepository;

import java.util.List;

public class WatchlistViewModel extends AndroidViewModel {

    private AppRepository repository;
    private MediatorLiveData<Resource<List<TvShow>>> watchlistListObservable = new MediatorLiveData<>();

    public WatchlistViewModel(@NonNull Application application) {
        super(application);
        repository = new AppRepository(application);
        watchlistListObservable.addSource(repository.getWatchlistListObservable(), new Observer<Resource<List<TvShow>>>() {
            @Override
            public void onChanged(Resource<List<TvShow>> tvShows) {
                watchlistListObservable.setValue(tvShows);
            }
        });
    }

    public LiveData<Resource<List<TvShow>>> getWatchlistListObservable() {
        return watchlistListObservable;
    }

    public void fetchData() {
        repository.fetchWatchlist();
    }

}
