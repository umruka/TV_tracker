package com.example.tvtracker.Models.QueryModels;

import com.example.tvtracker.Models.TvShow;
import com.example.tvtracker.Models.TvShowEpisode;
import com.example.tvtracker.Models.TvShowGenre;
import com.example.tvtracker.Models.TvShowPicture;

import java.util.List;

public class TvShowTest {
    private TvShow tvShow;
    private List<TvShowEpisode> tvShowEpisodes;
    private List<TvShowGenre> tvShowGenres;
    private List<TvShowPicture> tvShowPictures;

    public TvShowTest(TvShow tvShow, List<TvShowEpisode> tvShowEpisodes, List<TvShowGenre> tvShowGenres, List<TvShowPicture> tvShowPictures) {
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

    public int getEpisodeProgress () {
        List<TvShowEpisode> tvShowEpisodes = getTvShowEpisodes();
        int counter = 0;
        for(int i = 0; i < tvShowEpisodes.size();i++) {
            if (tvShowEpisodes.get(i).isWatched()) {
                counter++;
            }
        }
        return counter;
    }
}
