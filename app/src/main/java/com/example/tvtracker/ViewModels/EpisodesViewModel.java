package com.example.tvtracker.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.example.tvtracker.Models.Basic.Resource;
import com.example.tvtracker.Models.Params.SeasonTvShowParams;
import com.example.tvtracker.Models.Params.UpdateTvShowEpisodeWatchedFlagParams;
import com.example.tvtracker.Models.TvShowEpisode;
import com.example.tvtracker.Models.TvShowSeason;
import com.example.tvtracker.Repository.AppRepository;
import com.google.gson.Gson;

import java.util.List;

public class EpisodesViewModel extends AndroidViewModel {
    // TODO: Implement the ViewModel
    private AppRepository repository;
    private MediatorLiveData<TvShowSeason> seasonObservable = new MediatorLiveData<>();

    public EpisodesViewModel(@NonNull Application application) {
        super(application);
        repository = new AppRepository(application);
        seasonObservable.addSource(repository.getSeasonObservable(), new Observer<TvShowSeason>() {
            @Override
            public void onChanged(TvShowSeason tvShowSeason) {
                seasonObservable.setValue(tvShowSeason);
            }
        });
//        season2 = Transformations.switchMap(seasonParams, data -> repository.fetchSeasons2(seasonParams.getValue().getId(), seasonParams.getValue().getSeasonNum()));

    }

//    public void setSeasonParams(int id, int seasonNum) {
//        seasonParams.setValue(new SeasonTvShowParams(id, seasonNum));
//    }

    public LiveData<TvShowSeason> getSeasonObservable() {
        return seasonObservable;
    }


    public void getSeasonEpisodes(int id, int seasonNum){
        repository.fetchTvShowEpisodesBySeason(id, seasonNum);
    }

    public int getSeasonEpisodesCount() {
        TvShowSeason season = seasonObservable.getValue();
        List<TvShowEpisode> tvShowEpisodes = season.getEpisodes();
        return tvShowEpisodes.size();
    }

    public int getSeasonProgres() {
        TvShowSeason season = seasonObservable.getValue();
        return season.getSeasonProgress();
    }

    public void setWatchedFlag(UpdateTvShowEpisodeWatchedFlagParams params) { repository.updateTvShowEpisodeIsWatchedFlag(params); }

}
