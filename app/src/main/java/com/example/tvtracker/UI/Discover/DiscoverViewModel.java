package com.example.tvtracker.UI.Discover;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.tvtracker.UI.MainActivity;
import com.example.tvtracker.Models.TvShow;
import com.example.tvtracker.Repository.AppRepository;

import java.util.List;


public class DiscoverViewModel extends AndroidViewModel {
    private AppRepository repository;
    private final LiveData<List<TvShow>> discoverListObservable;

    public DiscoverViewModel(@NonNull Application application) {
        super(application);
        repository = new AppRepository(application);
        discoverListObservable = repository.fetchDiscover2();

        if(MainActivity.TEST_MODE) {
            repository.fetchTestDetails();
        }
    }

    LiveData<List<TvShow>> getDiscoverListObservable() {
        return discoverListObservable;
    }

}
