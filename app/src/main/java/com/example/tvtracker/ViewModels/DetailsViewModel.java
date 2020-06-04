package com.example.tvtracker.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;

import com.example.tvtracker.Models.Basic.Resource;
import com.example.tvtracker.Models.Params.UpdateTvShowEpisodeWatchedFlagParams;
import com.example.tvtracker.Models.Params.UpdateTvShowWatchingFlagParams;
import com.example.tvtracker.Models.QueryModels.TvShowFull;
import com.example.tvtracker.Models.TvShowEpisode;
import com.example.tvtracker.Models.TvShowSeason;
import com.example.tvtracker.Repository.AppRepository;

import java.util.ArrayList;
import java.util.List;

public class DetailsViewModel extends AndroidViewModel {
    private AppRepository repository;

    private MediatorLiveData<Resource<TvShowFull>> tvShowTestObservable = new MediatorLiveData<>();
    public DetailsViewModel(@NonNull Application application) {
    super(application);
    repository = new AppRepository(application);
        tvShowTestObservable.addSource(repository.getDetailObservable(), new Observer<Resource<TvShowFull>>() {
        @Override
        public void onChanged(Resource<TvShowFull> tvShowTestResource) {
            tvShowTestObservable.setValue(tvShowTestResource);
        }
    });

    }

    public MediatorLiveData<Resource<TvShowFull>> getTvShowTestObservable() {
        return tvShowTestObservable;
    }

    public void getDetails(int id) {
        repository.fetchTvShowDetails(id);
    }


    public void setWatchedFlag(UpdateTvShowEpisodeWatchedFlagParams params) { repository.updateTvShowEpisodeIsWatchedFlag(params); }

    public void setTvShowWatchedFlag(UpdateTvShowWatchingFlagParams params) { repository.updateTvShowWatchingFlag(params);
    }

    public boolean getShowState(){
        String isWatched = tvShowTestObservable.getValue().data.getTvShow().getTvShowWatchingFlag();
        if(("YES").equals(isWatched)){
            return true;
        }
        return false;
    }


}
