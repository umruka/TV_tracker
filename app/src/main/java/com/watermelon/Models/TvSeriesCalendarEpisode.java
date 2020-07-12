package com.watermelon.Models;

import androidx.room.Embedded;

public class TvSeriesCalendarEpisode {

    @Embedded
    public TvSeries tvSeries;

    @Embedded
    public TvSeriesEpisode episode;

    public TvSeries getTvSeries() {
        return tvSeries;
    }

    public void setTvSeries(TvSeries tvSeries) {
        this.tvSeries = tvSeries;
    }

    public TvSeriesEpisode getEpisode() {
        return episode;
    }

    public void setEpisode(TvSeriesEpisode episode) {
        this.episode = episode;
    }
}
