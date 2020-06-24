package com.example.tvtracker.DTO.Models.QueryModels;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.example.tvtracker.DTO.Models.TvShow;
import com.example.tvtracker.DTO.Models.TvShowEpisode;
import com.example.tvtracker.DTO.Models.TvShowGenre;
import com.example.tvtracker.DTO.Models.TvShowPicture;

import java.util.List;

public class fromDbCall {
    @Embedded public TvShow tvShow;
    @Relation(
            parentColumn = "tv_show_id",
            entityColumn = "tv_show_id"
    )
    public List<TvShowPicture> tvShowPictures;
    @Relation(
            parentColumn = "tv_show_id",
            entityColumn = "tv_show_id"
    )
    public List<TvShowEpisode> tvShowEpisodes;
    @Relation(
            parentColumn = "tv_show_id",
            entityColumn = "tv_show_id"
    )
    public List<TvShowGenre> tvShowGenres;
}
