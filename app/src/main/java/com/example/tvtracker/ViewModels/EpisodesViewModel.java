package com.example.tvtracker.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModel;

import com.example.tvtracker.Models.Params.UpdateTvShowEpisodeWatchedFlagParams;
import com.example.tvtracker.Models.TvShowSeason;
import com.example.tvtracker.Repository.AppRepository;
import com.google.gson.Gson;

public class EpisodesViewModel extends AndroidViewModel {
    // TODO: Implement the ViewModel
    private AppRepository repository;
    private TvShowSeason tvShowSeason;

    public EpisodesViewModel(@NonNull Application application) {
        super(application);
        repository = new AppRepository(application);
    }

    public void setTvShowSeason(String jsonData) {
        Gson gson = new Gson();
        this.tvShowSeason = gson.fromJson(jsonData, TvShowSeason.class);
    }

    public TvShowSeason getTvShowSeason() {
        return tvShowSeason;
    }

    public void setWatchedFlag(UpdateTvShowEpisodeWatchedFlagParams params) { repository.updateTvShowEpisodeIsWatchedFlag(params); }
}
