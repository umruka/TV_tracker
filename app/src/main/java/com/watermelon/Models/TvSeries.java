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
    private int tvShowId;
    @ColumnInfo(name = "tv_series_name")
    private String tvShowName;
    @ColumnInfo(name = "tv_series_status")
    private String tvShowStatus;
    @ColumnInfo(name = "tv_series_start_date")
    private String tvShowStartDate;
    @ColumnInfo(name = "tv_series_end_date")
    private String tvShowEndDate;
    @ColumnInfo(name = "tv_series_country")
    private String tvShowCountry;
    @ColumnInfo(name = "tv_series_network")
    private String tvShowNetwork;
    @ColumnInfo(name = "tv_series_image_path")
    private String tvShowImagePath;
    @ColumnInfo(name = "tv_series_runtime")
    private String tvShowRuntime;
    @ColumnInfo(name = "tv_series_description")
    private String tvShowDesc;
    @ColumnInfo(name = "tv_series_youtube_link")
    private String tvShowYoutubeLink;
    @ColumnInfo(name = "tv_series_rating")
    private String tvShowRating;
    @ColumnInfo(name = "tv_series_flag")
    private boolean tvShowWatchingFlag;


    public TvSeries(int tvShowId, String tvShowName, String tvShowStartDate, String tvShowEndDate, String tvShowCountry, String tvShowNetwork, String tvShowStatus, String tvShowImagePath) {
        this.tvShowId = tvShowId;
        this.tvShowName = tvShowName;
        this.tvShowStartDate = tvShowStartDate;
        this.tvShowEndDate = tvShowEndDate;
        this.tvShowCountry = tvShowCountry;
        this.tvShowNetwork = tvShowNetwork;
        this.tvShowStatus = tvShowStatus;
        this.tvShowImagePath = tvShowImagePath;
        this.tvShowRuntime = "";
        this.tvShowDesc = "";
        this.tvShowYoutubeLink = "";
        this.tvShowRating = "";
        this.tvShowWatchingFlag = false;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isTvShowWatchingFlag() {
        return tvShowWatchingFlag;
    }

    public int getTvShowId() {
        return tvShowId;
    }

    public void setTvShowId(int tvShowId) {
        this.tvShowId = tvShowId;
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

    public String getTvShowStartDate() {
        return tvShowStartDate;
    }

    public void setTvShowStartDate(String tvShowStartDate) {
        this.tvShowStartDate = tvShowStartDate;
    }

    public String getTvShowEndDate() {
        return tvShowEndDate;
    }

    public void setTvShowEndDate(String tvShowEndDate) {
        this.tvShowEndDate = tvShowEndDate;
    }

    public String getTvShowCountry() {
        return tvShowCountry;
    }

    public void setTvShowCountry(String tvShowCountry) {
        this.tvShowCountry = tvShowCountry;
    }

    public String getTvShowRuntime() {
        return tvShowRuntime;
    }

    public void setTvShowRuntime(String tvShowRuntime) {
        this.tvShowRuntime = tvShowRuntime;
    }

    public String getTvShowNetwork() {
        return tvShowNetwork;
    }

    public void setTvShowNetwork(String tvShowNetwork) {
        this.tvShowNetwork = tvShowNetwork;
    }

    public String getTvShowImagePath() {
        return tvShowImagePath;
    }

    public void setTvShowImagePath(String tvShowImagePath) {
        this.tvShowImagePath = tvShowImagePath;
    }

    public String getTvShowDesc() {
        return tvShowDesc;
    }

    public void setTvShowDesc(String tvShowDesc) {
        this.tvShowDesc = tvShowDesc;
    }

    public String getTvShowYoutubeLink() {
        return tvShowYoutubeLink;
    }

    public void setTvShowYoutubeLink(String tvShowYoutubeLink) {
        this.tvShowYoutubeLink = tvShowYoutubeLink;
    }

    public String getTvShowRating() {
        return tvShowRating;
    }

    public void setTvShowRating(String tvShowRating) {
        this.tvShowRating = tvShowRating;
    }


    public void setTvShowWatchingFlag(boolean tvShowWatchingFlag) {
        this.tvShowWatchingFlag = tvShowWatchingFlag;
    }
}
