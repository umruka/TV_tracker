package com.example.tvtracker.UI.SeasonEpisodes;

import android.app.Application;
import android.util.Pair;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;

import com.example.tvtracker.Models.TvShowSeason;
import com.example.tvtracker.Repository.AppRepository;

public class SeasonEpisodesViewModel extends AndroidViewModel {
    // TODO: Implement the ViewModel
    private AppRepository repository;
    private MediatorLiveData<TvShowSeason> seasonObservable = new MediatorLiveData<>();

    public SeasonEpisodesViewModel(@NonNull Application application) {
        super(application);
        repository = new AppRepository(application);
        seasonObservable.addSource(repository.getSeasonEpisodesListObservable(), new Observer<TvShowSeason>() {
            @Override
            public void onChanged(TvShowSeason tvShowSeason) {
                seasonObservable.setValue(tvShowSeason);
            }
        });
    }

    LiveData<TvShowSeason> getSeasonObservable() {
        return seasonObservable;
    }


    void getSeasonEpisodes(int id, int seasonNum) {
        repository.fetchTvShowEpisodesBySeason(id, seasonNum);
    }

    void changeEpisodeWatchedFlag(Pair<Integer, Boolean> params) {
        repository.setTvShowEpisodeIsWatchedFlag(params);
    }


}
