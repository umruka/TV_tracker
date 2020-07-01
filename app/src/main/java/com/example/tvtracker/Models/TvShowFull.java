package com.example.tvtracker.Models;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class TvShowFull {
    @Embedded
    public TvShow tvShow;

    @Relation(parentColumn = "tv_show_api_id", entityColumn = "genre_tv_show_id", entity = TvShowGenre.class)
    public List<TvShowGenre> genres;

    @Relation(parentColumn = "tv_show_api_id", entityColumn = "episode_tv_show_id", entity = TvShowEpisode.class)
    public List<TvShowEpisode> episodes;

    @Relation(parentColumn = "tv_show_api_id", entityColumn = "picture_tv_show_id", entity = TvShowPicture.class)
    public List<TvShowPicture> pictures;


}
