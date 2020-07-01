package com.example.tvtracker.Models;

import java.util.List;

public class TvShowSeason {
    private int seasonNum;
    private List<TvShowEpisode> episodes;

    public TvShowSeason(int seasonNum, List<TvShowEpisode> episodes) {
        this.seasonNum = seasonNum;
        this.episodes = episodes;
    }

    public List<TvShowEpisode> getEpisodes() {
        return episodes;
    }

    public void setEpisodes(List<TvShowEpisode> episodes) {
        this.episodes = episodes;
    }

    public int getSeasonNum() {
        return seasonNum;
    }

    public void setSeasonNum(int seasonNum) {
        this.seasonNum = seasonNum;
    }



}
