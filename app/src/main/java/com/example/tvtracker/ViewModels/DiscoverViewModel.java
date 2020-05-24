package com.example.tvtracker.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;

import com.example.tvtracker.Models.Basic.Resource;
import com.example.tvtracker.Models.QueryModels.TvShowFull;
import com.example.tvtracker.Models.QueryModels.TvShowTest;
import com.example.tvtracker.Models.TvShow;
import com.example.tvtracker.Models.TvShowEpisode;
import com.example.tvtracker.Models.TvShowPicture;
import com.example.tvtracker.Repository.AppRepository;
import com.example.tvtracker.Models.Params.UpdateTvShowWatchingFlagParams;

import java.util.List;

public class DiscoverViewModel extends AndroidViewModel {
    private AppRepository repository;
//    private WebServiceRepository webServiceRepository;
//    private LiveData<List<TvShow>> allTvShows;
    private MediatorLiveData<Resource<List<TvShow>>> discoverListObservable = new MediatorLiveData<>();
    private LiveData<List<TvShowTest>> allWatchingTvShows;
    private LiveData<List<TvShow>> allSearchWordTvShows;
//    private final LiveData<List<TvShow>> retroObservable;

    public DiscoverViewModel(@NonNull Application application) {
        super(application);
        repository = new AppRepository(application);

        discoverListObservable.addSource(repository.getDiscoverListObservable(), new Observer<Resource<List<TvShow>>>() {
            @Override
            public void onChanged(Resource<List<TvShow>> tvShows) {
                discoverListObservable.setValue(tvShows);
            }
        });
        allSearchWordTvShows = repository.getAllSearchTvShows();
    }

    public void getDiscoverData() { repository.fetchDiscoverData(); }

    public LiveData<Resource<List<TvShow>>> getDiscoverListObservable() {
        return discoverListObservable;
    }


    public LiveData<List<TvShow>> getAllSearchWordTvShows() {
        return allSearchWordTvShows;
    }

    public void updateTvShowBasicWatchingFlag(UpdateTvShowWatchingFlagParams params) {
        repository.updateTvShowWatchingFlag(params);
    }


    public void searchWord(String searchWord, int pageNum) {
        repository.searchTvShow(searchWord, pageNum);
    }

    public List<TvShowPicture> tvShowPicturesById(int showId) { return repository.getTvShowPicturesByShowId(showId);}

    public List<TvShowEpisode> tvShowEpisodesById(int showId) { return repository.getTvShowEpisodesById(showId);}


    public List<TvShowFull> getTvShowWithPicturesById(int showId) { return repository.getTvShowWithPicturesAndEpisodesById(showId);}

    public void clearSearchedTvShows() { repository.clearSearchedTvShows();}


    public void fetchDetailsForWatchlist(int id) {
        repository.fetchTvShowDetails(id);
    }

}
