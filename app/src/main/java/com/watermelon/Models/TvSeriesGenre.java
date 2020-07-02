package com.watermelon.Models;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "tv_series_genre_table", foreignKeys = @ForeignKey(entity = TvSeries.class,
        parentColumns = "tv_series_api_id",
        childColumns = "genre_tv_series_id",
        onDelete = ForeignKey.CASCADE))
public class TvSeriesGenre {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;
    @ColumnInfo(name = "genre_tv_series_id")
    private int genreTvSeriesId;
    @ColumnInfo(name = "genre_name")
    private String genreName;

    public TvSeriesGenre(int genreTvSeriesId, String genreName) {
        this.genreTvSeriesId = genreTvSeriesId;
        this.genreName = genreName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getGenreTvSeriesId() {
        return genreTvSeriesId;
    }

    public void setGenreTvSeriesId(int genreTvSeriesId) {
        this.genreTvSeriesId = genreTvSeriesId;
    }

    public String getGenreName() {
        return genreName;
    }

    public void setGenreName(String genreName) {
        this.genreName = genreName;
    }
}
