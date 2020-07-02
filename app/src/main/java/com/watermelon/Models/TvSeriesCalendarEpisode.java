package com.watermelon.Models;

import androidx.room.Embedded;

public class TvSeriesCalendarEpisode {

    @Embedded
    public TvSeries tvSeries;

    @Embedded
    public TvSeriesEpisode episode;


}
