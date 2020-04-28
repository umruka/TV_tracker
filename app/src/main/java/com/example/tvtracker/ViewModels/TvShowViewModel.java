package com.example.tvtracker.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.tvtracker.Models.QueryModels.TvShowFull;
import com.example.tvtracker.Models.TvShow;
import com.example.tvtracker.Models.TvShowEpisode;
import com.example.tvtracker.Models.TvShowPicture;
import com.example.tvtracker.Repository.AppRepository;
import com.example.tvtracker.Models.Params.UpdateTvShowWatchingFlagParams;

import java.util.List;

public class TvShowViewModel extends AndroidViewModel {
    private AppRepository repository;
    private LiveData<List<TvShow>> allTvShows;
    private LiveData<List<TvShow>> allWatchingTvShows;

    public TvShowViewModel(@NonNull Application application) {
        super(application);
        repository = new AppRepository(application);
        allTvShows = repository.getAllTvShows();
        allWatchingTvShows = repository.getAllWatchingTvShows();
    }

    public LiveData<List<TvShow>> getAllTvShows() {
        return allTvShows;
    }

    public LiveData<List<TvShow>> getAllWatchingTvShows() {
        return allWatchingTvShows;
    }

    public void insertTvShowBasic(TvShow tvShow) {
        repository.insertTvShow(tvShow);
    }

    public void updateTvShowBasic(TvShow tvShow) {
        repository.updateTvShow(tvShow);
    }

    public void updateTvShowBasicWatchingFlag(UpdateTvShowWatchingFlagParams params) {
        repository.updateTvShowWatchingFlag(params);
    }

    public void deleteTvShowBasic(int id) {
        repository.deleteTvShow(id);
    }

    public void deleteAllTvShowsBasic() {
        repository.deleteAllTvShows();
    }

    public TvShow getTvShowBasic(int id) {
        return repository.getTvShowById(id);
    }

    public void syncTvShowBasicFromApi() {
        repository.insertMostPopularTvShowsBasicInfo();
    }

    public void searchWord(String searchWord, int pageNum) {
        repository.searchTvShow(searchWord, pageNum);
    }

    public void syncTvShowDetailsFromApi(int id) { repository.insertTvShowDetailsInfo(id);}

    public List<TvShowPicture> tvShowPicturesById(int showId) { return repository.getTvShowPicturesByShowId(showId);}

    public List<TvShowEpisode> tvShowEpisodesById(int showId) { return repository.getTvShowEpisodesById(showId);}


    public List<TvShowFull> getTvShowWithPicturesById(int showId) { return repository.getTvShowWithPicturesAndEpisodesById(showId);}
}
