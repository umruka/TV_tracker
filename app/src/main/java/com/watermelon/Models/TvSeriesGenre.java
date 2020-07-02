package com.watermelon.Models;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "tv_series_genre_table", foreignKeys = @ForeignKey(entity = TvSeries.class,
        parentColumns = "tv_series_api_id",
        childColumns = "genre_tv_show_id",
        onDelete = ForeignKey.CASCADE))
public class TvSeriesGenre {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;
    @ColumnInfo(name = "genre_tv_show_id")
    private int genreTvShowId;
    @ColumnInfo(name = "genre_name")
    private String genreName;

    public TvSeriesGenre(int genreTvShowId, String genreName) {
        this.genreTvShowId = genreTvShowId;
        this.genreName = genreName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getGenreTvShowId() {
        return genreTvShowId;
    }

    public void setGenreTvShowId(int genreTvShowId) {
        this.genreTvShowId = genreTvShowId;
    }

    public String getGenreName() {
        return genreName;
    }

    public void setGenreName(String genreName) {
        this.genreName = genreName;
    }
}
