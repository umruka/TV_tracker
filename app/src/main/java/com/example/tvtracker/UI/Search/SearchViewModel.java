package com.example.tvtracker.UI.Search;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.tvtracker.Models.TvShow;
import com.example.tvtracker.UI.MainActivity;
import com.example.tvtracker.Repository.AppRepository;

import java.util.List;


public class SearchViewModel extends AndroidViewModel {
    private AppRepository repository;
    private final LiveData<List<TvShow>> discoverList;
    private final LiveData<List<TvShow>> allSearchWordTvShows;

    public SearchViewModel(@NonNull Application application) {
        super(application);
        repository = new AppRepository(application);
        discoverList = repository.fetchDiscover2();
        allSearchWordTvShows = repository.getSearchTvShowsListObservable();

        if (MainActivity.TEST_MODE) {
            repository.fetchTestDetails();
        }
    }

    LiveData<List<TvShow>> getDiscoverList() {
        return discoverList;
    }

    LiveData<List<TvShow>> getAllSearchWordTvShows() {
        return allSearchWordTvShows;
    }

    void searchWord(String searchWord) {
        repository.searchTvShow(searchWord, 1);
    }

    void clearSearchedTvShows() {
        repository.clearSearchedTvShows();
    }

    void fetchTvShowDetails(int id) {
        repository.fetchTvShowDetailsFromSearch(id);
    }


}
