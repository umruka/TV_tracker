package com.example.tvtracker.UI.Details;

import android.app.Application;
import android.util.Pair;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.example.tvtracker.DTO.Models.Basic.Resource;
import com.example.tvtracker.DTO.Models.Params.UpdateTvShowWatchingFlagParams;
import com.example.tvtracker.DTO.Models.QueryModels.TvShowFull;
import com.example.tvtracker.DTO.Models.TvShowGenre;
import com.example.tvtracker.Repository.AppRepository;

import java.util.List;

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

    public void setWatchedFlag(Pair<List<Integer>, Boolean> params) { repository.setTvShowAllSeasonWatched(params); }

    public void setTvShowWatchedFlag(Pair<Integer, Boolean> params) { repository.setTvShowWatchingFlag(params);
    }
    public boolean getShowState(){
        boolean isWatched = detailsObservable.getValue().data.getTvShow().isTvShowWatchingFlag();
        if(isWatched){
            return true;
        }
        return false;
    }

    public String getGenresString() {
        String genresString = "";
        List<TvShowGenre> genres = detailsObservable.getValue().data.getTvShowGenres();
        for (TvShowGenre genre : genres){
            if(genre.equals(genres.get(genres.size() - 1))) {
                genresString += genre.getTvShowGenreName();
            }else{
                genresString += genre.getTvShowGenreName() + ", ";
            }
        }
        return genresString;
    }

    public String getTvShowRatingString(String rating){
        if (rating.length() >= 5) {
            return rating.substring(0, 4);
        } else {
            return rating;
        }
    }



}
