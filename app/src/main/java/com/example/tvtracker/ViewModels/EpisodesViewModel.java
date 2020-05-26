package com.example.tvtracker.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.example.tvtracker.Models.Basic.Resource;
import com.example.tvtracker.Models.Params.UpdateTvShowEpisodeWatchedFlagParams;
import com.example.tvtracker.Models.TvShowEpisode;
import com.example.tvtracker.Models.TvShowSeason;
import com.example.tvtracker.Repository.AppRepository;
import com.google.gson.Gson;

public class EpisodesViewModel extends AndroidViewModel {
    // TODO: Implement the ViewModel
    private AppRepository repository;
    private MediatorLiveData<Resource<TvShowSeason>> seasonObservable = new MediatorLiveData<>();

    public EpisodesViewModel(@NonNull Application application) {
        super(application);
        repository = new AppRepository(application);
        seasonObservable.addSource(repository.getSeasonObservable(), new Observer<Resource<TvShowSeason>>() {
            @Override
            public void onChanged(Resource<TvShowSeason> tvShowSeasonResource) {
                seasonObservable.setValue(tvShowSeasonResource);
            }
        });
    }

    public MediatorLiveData<Resource<TvShowSeason>> getSeasonObservable() {
        return seasonObservable;
    }

    public void getSeasonEpisodes(int id, int seasonNum){
        repository.fetchTvShowEpisodesBySeason(id, seasonNum);
    }

    public void setWatchedFlag(UpdateTvShowEpisodeWatchedFlagParams params) { repository.updateTvShowEpisodeIsWatchedFlag(params); }

}
