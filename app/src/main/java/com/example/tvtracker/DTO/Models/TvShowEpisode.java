package com.example.tvtracker.DTO.Models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "tv_show_episode_table")
public class TvShowEpisode {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;
    @ColumnInfo(name = "tv_show_id")
    private int tvShowId;
    @ColumnInfo(name = "tv_show_season")
    private int seasonNum;
    @ColumnInfo(name = "tv_show_episode")
    private int episodeNum;
    @ColumnInfo(name = "tv_show_episode_name")
    private String episodeName;
    @ColumnInfo(name = "tv_show_air_date")
    private String episodeAirDate;
    @ColumnInfo(name = "tv_show_is_watched")
    private boolean watched;

    public TvShowEpisode(int tvShowId, int seasonNum, int episodeNum, String episodeName, String episodeAirDate) {
        this.tvShowId = tvShowId;
        this.seasonNum = seasonNum;
        this.episodeNum = episodeNum;
        this.episodeName = episodeName;
        this.episodeAirDate = episodeAirDate;
        this.watched = false;
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

    public int getSeasonNum() {
        return seasonNum;
    }

    public void setSeasonNum(int seasonNum) {
        this.seasonNum = seasonNum;
    }

    public int getEpisodeNum() {
        return episodeNum;
    }

    public void setEpisodeNum(int episodeNum) {
        this.episodeNum = episodeNum;
    }

    public String getEpisodeName() {
        return episodeName;
    }

    public void setEpisodeName(String episodeName) {
        this.episodeName = episodeName;
    }

    public String getEpisodeAirDate() {
        return episodeAirDate;
    }

    public void setEpisodeAirDate(String episodeAirDate) {
        this.episodeAirDate = episodeAirDate;
    }

    public boolean isWatched() {
        return watched;
    }

    public void setWatched(boolean watched) {
        this.watched = watched;
    }
}
