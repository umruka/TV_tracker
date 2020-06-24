package com.example.tvtracker.UI.Details;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.example.tvtracker.DTO.Models.Basic.Resource;
import com.example.tvtracker.DTO.Models.Params.UpdateTvShowEpisodeWatchedFlagParams;
import com.example.tvtracker.DTO.Models.Params.UpdateTvShowWatchingFlagParams;
import com.example.tvtracker.DTO.Models.QueryModels.TvShowFull;
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

    public void setWatchedFlag(UpdateTvShowEpisodeWatchedFlagParams params) { repository.setTvShowEpisodeIsWatchedFlag(params); }

    public void setTvShowWatchedFlag(UpdateTvShowWatchingFlagParams params) { repository.setTvShowWatchingFlag(params);
    }
    public boolean getShowState(){
        boolean isWatched = detailsObservable.getValue().data.getTvShow().isTvShowWatchingFlag();
        if(isWatched){
            return true;
        }
        return false;
    }


}