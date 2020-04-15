package com.example.tvtracker.WatchlistModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.tvtracker.Repository.TvShowRepository;
import com.example.tvtracker.TvShowModel.TvShow;

import java.util.List;

public class WatchlistViewModel extends AndroidViewModel {
    private TvShowRepository repository;
    private LiveData<List<TvShow>> allTvShowsWatching;

    public WatchlistViewModel(@NonNull Application application){
        super(application);
        repository = new TvShowRepository(application);
        allTvShowsWatching = repository.getAllWatchlistTvShows();
    }
    public LiveData<List<TvShow>> getAllWatchlist() {
        return allTvShowsWatching;
    }

}
