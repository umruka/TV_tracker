package com.watermelon.Models;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "tv_series_table", indices = {@Index(value = {"tv_series_api_id"}, unique = true)})
public class TvSeries {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "tv_series_id")
    private int id;

    @ColumnInfo(name = "tv_series_api_id")
    private int tvSeriesId;
    @ColumnInfo(name = "tv_series_name")
    private String tvSeriesName;
    @ColumnInfo(name = "tv_series_status")
    private String tvSeriesStatus;
    @ColumnInfo(name = "tv_series_start_date")
    private String tvSeriesStartDate;
    @ColumnInfo(name = "tv_series_end_date")
    private String tvSeriesEndDate;
    @ColumnInfo(name = "tv_series_country")
    private String tvSeriesCountry;
    @ColumnInfo(name = "tv_series_network")
    private String tvSeriesNetwork;
    @ColumnInfo(name = "tv_series_image_path")
    private String tvSeriesImagePath;
    @ColumnInfo(name = "tv_series_runtime")
    private String tvSeriesRuntime;
    @ColumnInfo(name = "tv_series_description")
    private String tvSeriesDesc;
    @ColumnInfo(name = "tv_series_youtube_link")
    private String tvSeriesYoutubeLink;
    @ColumnInfo(name = "tv_series_rating")
    private String tvSeriesRating;
    @ColumnInfo(name = "tv_series_flag")
    private boolean tvSeriesWatchingFlag;


    public TvSeries(int tvSeriesId, String tvSeriesName, String tvSeriesStartDate, String tvSeriesEndDate, String tvSeriesCountry, String tvSeriesNetwork, String tvSeriesStatus, String tvSeriesImagePath) {
        this.tvSeriesId = tvSeriesId;
        this.tvSeriesName = tvSeriesName;
        this.tvSeriesStartDate = tvSeriesStartDate;
        this.tvSeriesEndDate = tvSeriesEndDate;
        this.tvSeriesCountry = tvSeriesCountry;
        this.tvSeriesNetwork = tvSeriesNetwork;
        this.tvSeriesStatus = tvSeriesStatus;
        this.tvSeriesImagePath = tvSeriesImagePath;
        this.tvSeriesRuntime = "";
        this.tvSeriesDesc = "";
        this.tvSeriesYoutubeLink = "";
        this.tvSeriesRating = "";
        this.tvSeriesWatchingFlag = false;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isTvSeriesWatchingFlag() {
        return tvSeriesWatchingFlag;
    }

    public int getTvSeriesId() {
        return tvSeriesId;
    }

    public void setTvSeriesId(int tvSeriesId) {
        this.tvSeriesId = tvSeriesId;
    }

    public String getTvSeriesName() {
        return tvSeriesName;
    }

    public void setTvSeriesName(String tvSeriesName) {
        this.tvSeriesName = tvSeriesName;
    }

    public String getTvSeriesStatus() {
        return tvSeriesStatus;
    }

    public void setTvSeriesStatus(String tvSeriesStatus) {
        this.tvSeriesStatus = tvSeriesStatus;
    }

    public String getTvSeriesStartDate() {
        return tvSeriesStartDate;
    }

    public void setTvSeriesStartDate(String tvSeriesStartDate) {
        this.tvSeriesStartDate = tvSeriesStartDate;
    }

    public String getTvSeriesEndDate() {
        return tvSeriesEndDate;
    }

    public void setTvSeriesEndDate(String tvSeriesEndDate) {
        this.tvSeriesEndDate = tvSeriesEndDate;
    }

    public String getTvSeriesCountry() {
        return tvSeriesCountry;
    }

    public void setTvSeriesCountry(String tvSeriesCountry) {
        this.tvSeriesCountry = tvSeriesCountry;
    }

    public String getTvSeriesRuntime() {
        return tvSeriesRuntime;
    }

    public void setTvSeriesRuntime(String tvSeriesRuntime) {
        this.tvSeriesRuntime = tvSeriesRuntime;
    }

    public String getTvSeriesNetwork() {
        return tvSeriesNetwork;
    }

    public void setTvSeriesNetwork(String tvSeriesNetwork) {
        this.tvSeriesNetwork = tvSeriesNetwork;
    }

    public String getTvSeriesImagePath() {
        return tvSeriesImagePath;
    }

    public void setTvSeriesImagePath(String tvSeriesImagePath) {
        this.tvSeriesImagePath = tvSeriesImagePath;
    }

    public String getTvSeriesDesc() {
        return tvSeriesDesc;
    }

    public void setTvSeriesDesc(String tvSeriesDesc) {
        this.tvSeriesDesc = tvSeriesDesc;
    }

    public String getTvSeriesYoutubeLink() {
        return tvSeriesYoutubeLink;
    }

    public void setTvSeriesYoutubeLink(String tvSeriesYoutubeLink) {
        this.tvSeriesYoutubeLink = tvSeriesYoutubeLink;
    }

    public String getTvSeriesRating() {
        return tvSeriesRating;
    }

    public void setTvSeriesRating(String tvSeriesRating) {
        this.tvSeriesRating = tvSeriesRating;
    }


    public void setTvSeriesWatchingFlag(boolean tvSeriesWatchingFlag) {
        this.tvSeriesWatchingFlag = tvSeriesWatchingFlag;
    }
}
