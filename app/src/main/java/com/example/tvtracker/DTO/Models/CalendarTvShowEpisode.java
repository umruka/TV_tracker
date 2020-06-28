package com.example.tvtracker.DTO.Models;

public class CalendarTvShowEpisode {
    private String tvShowName;
    private String tvShowImageThumbnail;
    private TvShowEpisode tvShowEpisode;

    public CalendarTvShowEpisode(String tvShowName, String tvShowImageThumbnail, TvShowEpisode tvShowEpisode) {
        this.tvShowName = tvShowName;
        this.tvShowImageThumbnail = tvShowImageThumbnail;
        this.tvShowEpisode = tvShowEpisode;
    }

    public String getTvShowName() {
        return tvShowName;
    }

    public void setTvShowName(String tvShowName) {
        this.tvShowName = tvShowName;
    }

    public String getTvShowImageThumbnail() {
        return tvShowImageThumbnail;
    }

    public void setTvShowImageThumbnail(String tvShowImageThumbnail) {
        this.tvShowImageThumbnail = tvShowImageThumbnail;
    }

    public TvShowEpisode getTvShowEpisode() {
        return tvShowEpisode;
    }

    public void setTvShowEpisode(TvShowEpisode tvShowEpisode) {
        this.tvShowEpisode = tvShowEpisode;
    }
}
