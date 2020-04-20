package com.example.tvtracker.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.tvtracker.Models.TvShowDetails;
import com.example.tvtracker.Repository.AppRepository;

import java.util.List;

public class TvShowDetailsViewModel extends AndroidViewModel {
    private AppRepository repository;
    private LiveData<List<TvShowDetails>> allTvShowsFull;

    public TvShowDetailsViewModel(@NonNull Application application) {
        super(application);
        repository = new AppRepository(application);
        allTvShowsFull = repository.getAllTvShowsDetails();
    }

    public LiveData<List<TvShowDetails>> getAllTvShowsFull() {
        return allTvShowsFull;
    }

    public void insertTvShowDetails(int id){
        repository.insertTvShowDetailsInfo(id);
    }

}
