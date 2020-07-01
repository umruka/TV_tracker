package com.example.tvtracker.Models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "tv_show_picture_table", foreignKeys = @ForeignKey(entity = TvShow.class,
        parentColumns = "tv_show_api_id",
        childColumns = "picture_tv_show_id",
        onDelete = ForeignKey.CASCADE))
public class TvShowPicture {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;
    @ColumnInfo(name = "picture_tv_show_id")
    private int pictureTvShowId;
    @ColumnInfo(name = "picture_image_path")
    private String pictureImagePath;

    public TvShowPicture(int pictureTvShowId, String pictureImagePath) {
        this.pictureTvShowId = pictureTvShowId;
        this.pictureImagePath = pictureImagePath;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPictureTvShowId() {
        return pictureTvShowId;
    }

    public void setPictureTvShowId(int pictureTvShowId) {
        this.pictureTvShowId = pictureTvShowId;
    }

    public String getPictureImagePath() {
        return pictureImagePath;
    }

    public void setPictureImagePath(String pictureImagePath) {
        this.pictureImagePath = pictureImagePath;
    }
}
