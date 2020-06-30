package com.example.tvtracker.UI.Search;

import android.app.Application;
import android.util.Pair;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.tvtracker.DTO.Models.Params.UpdateTvShowWatchingFlagParams;
import com.example.tvtracker.DTO.Models.TvShow;
import com.example.tvtracker.MainActivity;
import com.example.tvtracker.Repository.AppRepository;

import java.util.List;


public class SearchViewModel extends AndroidViewModel {
    private AppRepository repository;
    private final LiveData<List<TvShow>> discoverList;
    private List<TvShow> defaultSearchList;
    private LiveData<List<TvShow>> allSearchWordTvShows;

    public SearchViewModel(@NonNull Application application) {
        super(application);
        repository = new AppRepository(application);
        discoverList = repository.fetchDiscover2();
        allSearchWordTvShows = repository.getSearchTvShowsListObservable();

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

    public void updateTvShowBasicWatchingFlag(Pair<Integer, Boolean> params) {
        repository.setTvShowWatchingFlag(params);
    }

    public void setDefaultSearchList() {
        defaultSearchList.addAll(discoverList.getValue());
    }

    public List<TvShow> getDefaultSearchList() {
        return defaultSearchList;
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
