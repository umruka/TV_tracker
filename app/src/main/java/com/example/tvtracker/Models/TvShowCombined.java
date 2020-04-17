package com.example.tvtracker.Models;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.example.tvtracker.TvShowFullModel.TvShowFull;
import com.example.tvtracker.TvShowModel.TvShow;

import java.util.List;

public class TvShowCombined {
    @Embedded private TvShow tvShow;
    @Relation(
            parentColumn = "tv_show_id",
            entityColumn = "tv_show_full_id"
    )
    private List<TvShowFull> tvShowFull;

    public TvShowCombined(TvShow tvShow, List<TvShowFull> tvShowFull) {
        this.tvShow = tvShow;
        this.tvShowFull = tvShowFull;
    }

    public TvShow getTvShow() {
        return tvShow;
    }

    public void setTvShow(TvShow tvShow) {
        this.tvShow = tvShow;
    }

    public List<TvShowFull> getTvShowFull() {
        return tvShowFull;
    }

    public void setTvShowFull(List<TvShowFull> tvShowFull) {
        this.tvShowFull = tvShowFull;
    }
}
