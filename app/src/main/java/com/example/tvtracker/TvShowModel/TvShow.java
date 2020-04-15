package com.example.tvtracker.TvShowModel;


import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
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
private String tvShowWatchingFlag;

    public TvShow(String tvShowName, String tvShowStatus, int tvShowId){
        this.tvShowName = tvShowName;
        this.tvShowStatus = tvShowStatus;
        this.tvShowId = tvShowId;
    }

    @Ignore
    public TvShow(String tvShowName, String tvShowStatus, int tvShowId, String tvShowWatchingFlag) {
        this.tvShowName = tvShowName;
        this.tvShowStatus = tvShowStatus;
        this.tvShowId = tvShowId;
        this.tvShowWatchingFlag = tvShowWatchingFlag;
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

    public String getTvShowWatchingFlag() {
        return tvShowWatchingFlag;
    }

    public void setTvShowWatchingFlag(String tvShowWatchingFlag) {
        this.tvShowWatchingFlag = tvShowWatchingFlag;
    }
}
