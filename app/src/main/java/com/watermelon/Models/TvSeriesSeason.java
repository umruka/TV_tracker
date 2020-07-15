package com.watermelon.Models;

import java.util.List;

public class TvSeriesSeason {
    private int seasonNum;
    private List<TvSeriesEpisode> episodes;

    public TvSeriesSeason(int seasonNum, List<TvSeriesEpisode> episodes) {
        this.seasonNum = seasonNum;
        this.episodes = episodes;
    }

    public List<TvSeriesEpisode> getEpisodes() {
        return episodes;
    }

    public void setEpisodes(List<TvSeriesEpisode> episodes) {
        this.episodes = episodes;
    }

    public int getSeasonNum() {
        return seasonNum;
    }

    public void setSeasonNum(int seasonNum) {
        this.seasonNum = seasonNum;
    }



}
