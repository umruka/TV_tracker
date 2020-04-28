package com.example.tvtracker.Models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "tv_show_picture_table")
public class TvShowPicture {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "tv_show_picture_id")
    private int tvShowPictureId;
    @ColumnInfo(name = "tv_show_id")
    private int tvShowId;
    @ColumnInfo(name = "tv_show_image_path")
    private String tvShowPicturePath;

    public TvShowPicture(int tvShowId, String tvShowPicturePath) {
        this.tvShowId = tvShowId;
        this.tvShowPicturePath = tvShowPicturePath;
    }

    public int getTvShowPictureId() {
        return tvShowPictureId;
    }

    public void setTvShowPictureId(int tvShowPictureId) {
        this.tvShowPictureId = tvShowPictureId;
    }

    public int getTvShowId() {
        return tvShowId;
    }

    public void setTvShowId(int tvShowId) {
        this.tvShowId = tvShowId;
    }

    public String getTvShowPicturePath() {
        return tvShowPicturePath;
    }

    public void setTvShowPicturePath(String tvShowPicturePath) {
        this.tvShowPicturePath = tvShowPicturePath;
    }
}
