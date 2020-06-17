package com.example.tvtracker.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.Transformations;

import com.example.tvtracker.Models.Basic.Resource;
import com.example.tvtracker.Models.QueryModels.TvShowFull;
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

    public void refreshData() {
        repository.fetchWatchlist();;
    }

}
