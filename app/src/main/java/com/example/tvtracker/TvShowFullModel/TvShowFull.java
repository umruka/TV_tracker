package com.example.tvtracker.TvShowFullModel;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.tvtracker.TvShowModel.TvShow;

@Entity(tableName = "tv_show_full")
public class TvShowFull {

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "tv_show_full_id")
    private int tvShowdId;

    @ColumnInfo(name = "tv_show_full_description")
    private String tvShowDesc;

    @ColumnInfo(name = "tv_show_full_youtube_link")
    private String tvShowYoutubeLink;

    @ColumnInfo(name = "tv_show_full_rating")
    private String tvShowRating;

    @ColumnInfo(name = "tv_show_full_image_path")
    private String tvShowImagePath;

//    @ColumnInfo(name = "tv_show_full_genre_id")
//    private int genreId;

//    @ColumnInfo(name = "tv_show_full_picture_id")
//    private int pictureId;

//    @ColumnInfo(name = "tv_show_full_episode_id")
//    private int episodeId;


    public TvShowFull(int tvShowdId, String tvShowDesc, String tvShowYoutubeLink, String tvShowRating, String tvShowImagePath) {
        this.tvShowdId = tvShowdId;
        this.tvShowDesc = tvShowDesc;
        this.tvShowYoutubeLink = tvShowYoutubeLink;
        this.tvShowRating = tvShowRating;
        this.tvShowImagePath = tvShowImagePath;
    }

    public int getTvShowdId() {
        return tvShowdId;
    }

    public void setTvShowdId(int tvShowdId) {
        this.tvShowdId = tvShowdId;
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

    public String getTvShowImagePath() {
        return tvShowImagePath;
    }

    public void setTvShowImagePath(String tvShowImagePath) {
        this.tvShowImagePath = tvShowImagePath;
    }
}
