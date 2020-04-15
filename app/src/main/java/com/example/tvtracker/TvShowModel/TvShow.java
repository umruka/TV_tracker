package com.example.tvtracker.TvShowModel;


import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

/*
,
        foreignKeys = @ForeignKey(entity = TvShow.class,
        parentColumns = "tvShowId",
        childColumns = "watchlistTvShowId",
        onDelete = ForeignKey.CASCADE)
*/
@Entity(tableName = "tvshow_table")
public class TvShow {
@PrimaryKey(autoGenerate = true)
private int id;
private String tvShowName;
private String tvShowStatus;
private int tvShowId;

    public TvShow(String tvShowName, String tvShowStatus, int tvShowId) {
        this.tvShowName = tvShowName;
        this.tvShowStatus = tvShowStatus;
        this.tvShowId = tvShowId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTvShowName() {
        return tvShowName;
    }

    public void setTvShowName(String tvShowName) {
        this.tvShowName = tvShowName;
    }

    public String getTvShowStatus() {
        return tvShowStatus;
    }

    public void setTvShowStatus(String tvShowStatus) {
        this.tvShowStatus = tvShowStatus;
    }

    public int getTvShowId() {
        return tvShowId;
    }

    public void setTvShowId(int tvShowId) {
        this.tvShowId = tvShowId;
    }
}
