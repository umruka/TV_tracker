package com.watermelon.Models;

import androidx.room.Embedded;

public class CalendarTvShowEpisode {

    @Embedded
    public TvSeries tvSeries;

    @Embedded
    public TvSeriesEpisode episode;


}
