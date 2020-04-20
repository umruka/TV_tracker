package com.example.tvtracker.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.tvtracker.Models.TvShowBasic;
import com.example.tvtracker.Repository.AppRepository;
import com.example.tvtracker.Models.UpdateTvShowBasicWatchingFlagParams;

import java.util.List;

public class TvShowBasicViewModel extends AndroidViewModel {
    private AppRepository repository;
    private LiveData<List<TvShowBasic>> allTvShows;

    public TvShowBasicViewModel(@NonNull Application application) {
        super(application);
        repository = new AppRepository(application);
        allTvShows = repository.getAllTvShows();
    }

    public LiveData<List<TvShowBasic>> getAllTvShows() {
        return allTvShows;
    }

    public void insertTvShowBasic(TvShowBasic tvShowBasic) {
        repository.insertTvShowBasic(tvShowBasic);
    }

    public void updateTvShowBasic(TvShowBasic tvShowBasic) {
        repository.updateTvShowBasic(tvShowBasic);
    }

    public void updateTvShowBasicWatchingFlag(UpdateTvShowBasicWatchingFlagParams params) {
        repository.updateTvShowBasicWatchingFlag(params);
    }

    public void deleteTvShowBasic(int id) {
        repository.deleteTvShowBasic(id);
    }

    public void deleteAllTvShowsBasic() {
        repository.deleteAllTvShowsBasic();
    }

    public TvShowBasic getTvShowBasic(int id) {
        return repository.getTvShowBasicById(id);
    }

    public void syncTvShowBasicFromApi() {
        repository.insertMostPopularTvShowsBasicInfo();
    }

    public void searchWord(String searchWord, int pageNum) {
        repository.searchTvShow(searchWord, pageNum);
    }

}
