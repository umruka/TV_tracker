package com.example.tvtracker.Models;

import androidx.room.Embedded;

public class CalendarTvShowEpisode {
    @Embedded
    public TvShow tvShow;

    @Embedded
    public TvShowEpisode episode;
}
