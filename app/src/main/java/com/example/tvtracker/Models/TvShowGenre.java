package com.example.tvtracker.Models;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "tv_show_genre_table")
public class TvShowGenre {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;
    @ColumnInfo(name = "tv_show_id")
    private int tvShowId;
    @ColumnInfo(name = "tv_show_genre_name")
    private String tvShowGenreName;

    public TvShowGenre(int tvShowId, String tvShowGenreName) {
        this.tvShowId = tvShowId;
        this.tvShowGenreName = tvShowGenreName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTvShowId() {
        return tvShowId;
    }

    public void setTvShowId(int tvShowId) {
        this.tvShowId = tvShowId;
    }

    public String getTvShowGenreName() {
        return tvShowGenreName;
    }

    public void setTvShowGenreName(String tvShowGenreName) {
        this.tvShowGenreName = tvShowGenreName;
    }
}
