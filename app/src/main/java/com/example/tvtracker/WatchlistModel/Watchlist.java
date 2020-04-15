package com.example.tvtracker.WatchlistModel;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "watchlist_table")
public class Watchlist {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private int watchlistTvShowId;

    public Watchlist(int watchlistTvShowId) {
        this.watchlistTvShowId = watchlistTvShowId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getWatchlistTvShowId() {
        return watchlistTvShowId;
    }

    public void setWatchlistTvShowId(int watchlistTvShowId) {
        this.watchlistTvShowId = watchlistTvShowId;
    }
}
