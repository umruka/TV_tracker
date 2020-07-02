package com.watermelon.UI.SeasonEpisodes;

import android.app.Application;
import android.util.Pair;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.watermelon.Models.TvSeriesSeason;
import com.watermelon.Repository.AppRepository;

public class SeasonEpisodesViewModel extends AndroidViewModel {
    // TODO: Implement the ViewModel
    private AppRepository repository;
    private LiveData<TvSeriesSeason> seasonEpisodesObservable;

    public SeasonEpisodesViewModel(@NonNull Application application) {
        super(application);
        repository = new AppRepository(application);
        seasonEpisodesObservable = repository.getSeasonEpisodesListObservable();
    }

    LiveData<TvSeriesSeason> getSeasonEpisodesObservable() {
        return seasonEpisodesObservable;
    }


    void getSeasonEpisodes(int id, int seasonNum) {
        repository.fetchTvSeriesEpisodesBySeason(id, seasonNum);
    }

    void changeEpisodeWatchedFlag(Pair<Integer, Boolean> params) {
        repository.setTvSeriesEpisodeWatchedFlag(params);
    }


}
