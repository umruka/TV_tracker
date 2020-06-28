package com.example.tvtracker.UI.Discover;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import com.example.tvtracker.MainActivity;
import com.example.tvtracker.DTO.Models.Basic.Resource;
import com.example.tvtracker.DTO.Models.TvShow;
import com.example.tvtracker.Repository.AppRepository;
import com.example.tvtracker.DTO.Models.Params.UpdateTvShowWatchingFlagParams;

import java.util.List;


public class DiscoverViewModel extends AndroidViewModel {
    private AppRepository repository;
    private MediatorLiveData<Resource<List<TvShow>>> discoverListObservable = new MediatorLiveData<>();
    private final LiveData<List<TvShow>> discoverList;
    private LiveData<List<TvShow>> allSearchWordTvShows;

    public DiscoverViewModel(@NonNull Application application) {
        super(application);
        repository = new AppRepository(application);
/*
        discoverListObservable.addSource(repository.getDiscoverListObservable(), new Observer<Resource<List<TvShow>>>() {
            @Override
            public void onChanged(Resource<List<TvShow>> tvShows) {
                discoverListObservable.setValue(tvShows);
            }
        });
 */
            discoverList = repository.fetchDiscover2();
        allSearchWordTvShows = repository.getAllSearchTvShows();


        if(MainActivity.TEST_MODE) {
            repository.fetchTestDetails();
        }
    }

    public LiveData<List<TvShow>> getDiscoverList() {
        return discoverList;
    }


    public LiveData<List<TvShow>> getAllSearchWordTvShows() {
        return allSearchWordTvShows;
    }

    public void updateTvShowBasicWatchingFlag(UpdateTvShowWatchingFlagParams params) {
        repository.setTvShowWatchingFlag(params);
    }


    public void searchWord(String searchWord, int pageNum) {
        repository.searchTvShow(searchWord, pageNum);
    }


    public void clearSearchedTvShows() { repository.clearSearchedTvShows();}

    public void addTvShowToDb(TvShow tvShow){
        repository.addTvShowToDb(tvShow);
    }

    public void fetchDetailsForWatchlist(int id) {

        repository.fetchTvShowDetailsFromSearch(id);
    }




}
