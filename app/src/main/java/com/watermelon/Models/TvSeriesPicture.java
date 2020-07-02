package com.watermelon.Models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "tv_series_picture_table", foreignKeys = @ForeignKey(entity = TvSeries.class,
        parentColumns = "tv_series_api_id",
        childColumns = "picture_tv_series_id",
        onDelete = ForeignKey.CASCADE))
public class TvSeriesPicture {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;
    @ColumnInfo(name = "picture_tv_series_id")
    private int pictureTvSeriesId;
    @ColumnInfo(name = "picture_image_path")
    private String pictureImagePath;

    public TvSeriesPicture(int pictureTvSeriesId, String pictureImagePath) {
        this.pictureTvSeriesId = pictureTvSeriesId;
        this.pictureImagePath = pictureImagePath;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPictureTvSeriesId() {
        return pictureTvSeriesId;
    }

    public void setPictureTvSeriesId(int pictureTvSeriesId) {
        this.pictureTvSeriesId = pictureTvSeriesId;
    }

    public String getPictureImagePath() {
        return pictureImagePath;
    }

    public void setPictureImagePath(String pictureImagePath) {
        this.pictureImagePath = pictureImagePath;
    }
}
