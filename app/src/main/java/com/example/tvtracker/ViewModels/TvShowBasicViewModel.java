package com.example.tvtracker.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.tvtracker.JsonModels.TvShowBasic.JsonTvShowBasicRoot;
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

    public void insert(TvShowBasic tvShowBasic) {
        repository.insertTvShowBasic(tvShowBasic);
    }

    public void deleteAllTvShows() {
        repository.deleteAllTvShowsBasic();
    }

    public void updateTvShow(TvShowBasic tvShowBasic) {
        repository.updateTvShow(tvShowBasic);
    }

    public void updateTvShowWatchingFlag(UpdateTvShowBasicWatchingFlagParams params) {
        repository.updateTvShowBasicWatchingFlag(params);
    }

    public void deleteTvShow(int id) {
        repository.deleteTvShowBasic(id);
    }

    public TvShowBasic getTvShow(int id) {
        return repository.getTvShowBasicById(id);
    }

    private void deleteNotInServer(JsonTvShowBasicRoot data) {
        boolean isForDeleting;
        for (int i = 0; i < allTvShows.getValue().size(); i++) {
            isForDeleting = true;
            for (int j = 0; j < data.getTVShows().size(); j++) {
                String tvShowDB = String.valueOf(allTvShows.getValue().get(i).getTvShowId());
                String tvShowServer = String.valueOf(data.getTVShows().get(j).getId());

                if (tvShowDB.compareTo(tvShowServer) == 0) {
                    isForDeleting = false;
                    break;
                }
            }
            if (isForDeleting) {
                repository.deleteTvShowBasic(allTvShows.getValue().get(i).getTvShowId());
            }
        }
    }

    public void syncTvShows() {
        repository.insertMostPopularTvShowsBasicInfo();
    }


}
