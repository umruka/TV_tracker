package com.example.tvtracker.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;

import com.example.tvtracker.Models.Basic.Resource;
import com.example.tvtracker.Models.Params.UpdateTvShowEpisodeWatchedFlagParams;
import com.example.tvtracker.Models.QueryModels.TvShowTest;
import com.example.tvtracker.Models.TvShow;
import com.example.tvtracker.Repository.AppRepository;

import java.util.List;

public class DetailsViewModel extends AndroidViewModel {
    private AppRepository repository;

    private MediatorLiveData<Resource<TvShowTest>> tvShowTestObservable = new MediatorLiveData<>();
    public DetailsViewModel(@NonNull Application application) {
    super(application);
    repository = new AppRepository(application);
        tvShowTestObservable.addSource(repository.getDetailObservable(), new Observer<Resource<TvShowTest>>() {
        @Override
        public void onChanged(Resource<TvShowTest> tvShowTestResource) {
            tvShowTestObservable.setValue(tvShowTestResource);
        }
    });

    }

    public MediatorLiveData<Resource<TvShowTest>> getTvShowTestObservable() {
        return tvShowTestObservable;
    }

    public void getDetails(int id) {
        repository.fetchTvShowDetails(id);
    }

    public void setWatchedFlag(UpdateTvShowEpisodeWatchedFlagParams params) { repository.updateTvShowEpisodeIsWatchedFlag(params); }
}
