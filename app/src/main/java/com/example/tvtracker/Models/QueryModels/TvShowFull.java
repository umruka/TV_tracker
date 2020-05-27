package com.example.tvtracker.Models.QueryModels;

import com.example.tvtracker.Models.TvShow;
import com.example.tvtracker.Models.TvShowEpisode;
import com.example.tvtracker.Models.TvShowGenre;
import com.example.tvtracker.Models.TvShowPicture;
import com.example.tvtracker.Models.TvShowSeason;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TvShowFull {
    private TvShow tvShow;
    private List<TvShowEpisode> tvShowEpisodes;
    private List<TvShowGenre> tvShowGenres;
    private List<TvShowPicture> tvShowPictures;

    public TvShowFull(TvShow tvShow, List<TvShowEpisode> tvShowEpisodes, List<TvShowGenre> tvShowGenres, List<TvShowPicture> tvShowPictures) {
        this.tvShow = tvShow;
        this.tvShowEpisodes = tvShowEpisodes;
        this.tvShowGenres = tvShowGenres;
        this.tvShowPictures = tvShowPictures;
    }

    public TvShow getTvShow() {
        return tvShow;
    }

    public void setTvShow(TvShow tvShow) {
        this.tvShow = tvShow;
    }

    public List<TvShowEpisode> getTvShowEpisodes() {
        return tvShowEpisodes;
    }

    public void setTvShowEpisodes(List<TvShowEpisode> tvShowEpisodes) {
        this.tvShowEpisodes = tvShowEpisodes;
    }

    public List<TvShowGenre> getTvShowGenres() {
        return tvShowGenres;
    }

    public void setTvShowGenres(List<TvShowGenre> tvShowGenres) {
        this.tvShowGenres = tvShowGenres;
    }

    public List<TvShowPicture> getTvShowPictures() {
        return tvShowPictures;
    }

    public void setTvShowPictures(List<TvShowPicture> tvShowPictures) {
        this.tvShowPictures = tvShowPictures;
    }

    public int getEpisodeProgress() {
        List<TvShowEpisode> tvShowEpisodes = getTvShowEpisodes();
        int counter = 0;
        for (int i = 0; i < tvShowEpisodes.size(); i++) {
            if (tvShowEpisodes.get(i).isWatched()) {
                counter++;
            }
        }
        return counter;
    }

    public List<TvShowSeason> getTvShowSeasons() {
        List<TvShowEpisode> tvShowEpisodes = getTvShowEpisodes();
        List<TvShowSeason> tvShowSeasons = new ArrayList<>();
        List<TvShowEpisode> currentSeasonEpisodes = new ArrayList<>();

        int currentSeason = 1;
        for (int i = 0; i < tvShowEpisodes.size(); i++) {
            TvShowEpisode currentEpisode = tvShowEpisodes.get(i);
            int currentEpisodeSeason = currentEpisode.getSeasonNum();
            if (currentEpisodeSeason == currentSeason) {
                currentSeasonEpisodes.add(currentEpisode);
            } else {
                tvShowSeasons.add(new TvShowSeason(currentSeason, currentSeasonEpisodes));
                currentSeason++;
                currentSeasonEpisodes = new ArrayList<>();
            }
        }
        return tvShowSeasons;
    }

    public TvShowEpisode getNextWatched() {

        List<TvShowEpisode> tvShowEpisodes = getTvShowEpisodes();
        for (int i = 0; i < tvShowEpisodes.size(); i++) {
            if (!tvShowEpisodes.get(i).isWatched()) {
                return tvShowEpisodes.get(i);
            }
        }
        return null;
    }

    public boolean getTvShowState() {
        List<TvShowEpisode> tvShowEpisodes = getTvShowEpisodes();
        boolean state = true;
        for (int i = 0; i < tvShowEpisodes.size(); i++) {
            if (!tvShowEpisodes.get(i).isWatched()) {
                state = false;
                return state;
            }
        }
        return state;
    }

    /*
                List<TvShowSeason> seasons = tvShowSeasons;
        for(int i = 0; i < seasons.size();i++) {
            int seasonEpisodeSize = seasons.get(i).getEpisodes().size();
            for(int j = 0; j < seasonEpisodeSize; j++) {
                boolean isCurrentEpisodeWatched = tvShowSeasons.get(i).getEpisodes().get(j).isWatched();
                if(isCurrentEpisodeWatched) {
                    counter++;
                }
            }
        }

         */


}
