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

    public void insertTvShowDetails(TvShowDetails tvShowDetails){
        repository.insertTvShowDetails(tvShowDetails);
    }

    public void updateTvShowDetails(TvShowDetails tvShowDetails) { repository.updateTvShowDetails(tvShowDetails);}

    public void deleteTvShowDetails(int id) { repository.deleteTvShowDetails(id);}

    public void deleteAllTvShowsDetails() { repository.deleteAllTvShowsDetail();}

    public void syncTvShowDetailsFromApi(int id) { repository.insertTvShowDetailsInfo(id);}

}
