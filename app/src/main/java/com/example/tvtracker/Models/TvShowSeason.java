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

    public int getSeasonProgress() {
        List<TvShowEpisode> tvShowEpisodes = getEpisodes();
        int counter = 0;
        for(int i = 0; i < tvShowEpisodes.size();i++) {
            if (tvShowEpisodes.get(i).isWatched()) {
                counter++;
            }
        }
        return counter;
    }

}
