package com.example.tvtracker.DTO.Models;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "tv_show_table")
public class TvShow {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "tv_show_id")
    private int id;
    @ColumnInfo(name = "tv_show_api_id")
    private int tvShowId;
    @ColumnInfo(name = "tv_show_name")
    private String tvShowName;
    @ColumnInfo(name = "tv_show_status")
    private String tvShowStatus;
    @ColumnInfo(name = "tv_show_start_date")
    private String tvShowStartDate;
    @ColumnInfo(name = "tv_show_end_date")
    private String tvShowEndDate;
    @ColumnInfo(name = "tv_show_country")
    private String tvShowCountry;
    @ColumnInfo(name = "tv_show_network")
    private String tvShowNetwork;
    @ColumnInfo(name = "tv_show_image_path")
    private String tvShowImagePath;
    @ColumnInfo(name = "tv_show_description")
    private String tvShowDesc;
    @ColumnInfo(name = "tv_show_youtube_link")
    private String tvShowYoutubeLink;
    @ColumnInfo(name = "tv_show_rating")
    private String tvShowRating;
    @ColumnInfo(name = "tv_show_flag")
    private boolean tvShowWatchingFlag;


    public TvShow(int tvShowId, String tvShowName, String tvShowStartDate, String tvShowEndDate, String tvShowCountry, String tvShowNetwork, String tvShowStatus, String tvShowImagePath) {
        this.tvShowId = tvShowId;
        this.tvShowName = tvShowName;
        this.tvShowStartDate = tvShowStartDate;
        this.tvShowEndDate = tvShowEndDate;
        this.tvShowCountry = tvShowCountry;
        this.tvShowNetwork = tvShowNetwork;
        this.tvShowStatus = tvShowStatus;
        this.tvShowImagePath = tvShowImagePath;
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
