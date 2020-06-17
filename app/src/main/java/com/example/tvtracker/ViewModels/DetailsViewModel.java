package com.example.tvtracker.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.example.tvtracker.Models.Basic.Resource;
import com.example.tvtracker.Models.Params.UpdateTvShowEpisodeWatchedFlagParams;
import com.example.tvtracker.Models.Params.UpdateTvShowWatchingFlagParams;
import com.example.tvtracker.Models.QueryModels.TvShowFull;
import com.example.tvtracker.Repository.AppRepository;

public class DetailsViewModel extends AndroidViewModel {
    private AppRepository repository;

    private final LiveData<Resource<TvShowFull>> detailsObservable;
    private MutableLiveData tvShowId = new MutableLiveData();
    public DetailsViewModel(@NonNull Application application) {
    super(application);
    repository = new AppRepository(application);
    detailsObservable = Transformations.switchMap(tvShowId, id -> repository.fetchTvShowDetails2((Integer) tvShowId.getValue())  );

    }

    public void setTvShowId(int id) {
        tvShowId.setValue(id);
    }

    public LiveData<Resource<TvShowFull>> getDetailsObservable() {
        return detailsObservable;
    }

    public void setWatchedFlag(UpdateTvShowEpisodeWatchedFlagParams params) { repository.updateTvShowEpisodeIsWatchedFlag(params); }

    public void setTvShowWatchedFlag(UpdateTvShowWatchingFlagParams params) { repository.updateTvShowWatchingFlag(params);
    }
    public boolean getShowState(){
        boolean isWatched = detailsObservable.getValue().data.getTvShow().isTvShowWatchingFlag();
        if(isWatched){
            return true;
        }
        return false;
    }


}
