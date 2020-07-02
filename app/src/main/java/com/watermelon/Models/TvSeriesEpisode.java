package com.watermelon.Models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "tv_series_episode_table", foreignKeys = @ForeignKey(entity = TvSeries.class,
        parentColumns = "tv_series_api_id",
        childColumns = "episode_tv_series_id",
        onDelete = ForeignKey.CASCADE))
public class TvSeriesEpisode {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;
    @ColumnInfo(name = "episode_tv_series_id")
    private int episodeTvSeriesId;
    @ColumnInfo(name = "episode_season")
    private int episodeSeasonNum;
    @ColumnInfo(name = "episode_number")
    private int episodeNum;
    @ColumnInfo(name = "episode_name")
    private String episodeName;
    @ColumnInfo(name = "episode_air_date")
    private String episodeAirDate;
    @ColumnInfo(name = "episode_is_watched")
    private boolean episodeWatched;

    public TvSeriesEpisode(int episodeTvSeriesId, int episodeSeasonNum, int episodeNum, String episodeName, String episodeAirDate) {
        this.episodeTvSeriesId = episodeTvSeriesId;
        this.episodeSeasonNum = episodeSeasonNum;
        this.episodeNum = episodeNum;
        this.episodeName = episodeName;
        this.episodeAirDate = episodeAirDate;
        this.episodeWatched = false;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getEpisodeTvSeriesId() {
        return episodeTvSeriesId;
    }

    public void setEpisodeTvSeriesId(int episodeTvSeriesId) {
        this.episodeTvSeriesId = episodeTvSeriesId;
    }

    public int getEpisodeSeasonNum() {
        return episodeSeasonNum;
    }

    public void setEpisodeSeasonNum(int episodeSeasonNum) {
        this.episodeSeasonNum = episodeSeasonNum;
    }

    public int getEpisodeNum() {
        return episodeNum;
    }

    public void setEpisodeNum(int episodeEpisodeNum) {
        this.episodeNum = episodeEpisodeNum;
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

    public boolean isEpisodeWatched() {
        return episodeWatched;
    }

    public void setEpisodeWatched(boolean episodeWatched) {
        this.episodeWatched = episodeWatched;
    }


}
