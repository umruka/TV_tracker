package com.watermelon.Models;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class TvSeriesFull {
    @Embedded
    public TvSeries tvSeries;

    @Relation(parentColumn = "tv_series_api_id", entityColumn = "genre_tv_series_id", entity = TvSeriesGenre.class)
    public List<TvSeriesGenre> genres;

    @Relation(parentColumn = "tv_series_api_id", entityColumn = "episode_tv_series_id", entity = TvSeriesEpisode.class)
    public List<TvSeriesEpisode> episodes;

    @Relation(parentColumn = "tv_series_api_id", entityColumn = "picture_tv_series_id", entity = TvSeriesPicture.class)
    public List<TvSeriesPicture> pictures;


    public TvSeriesFull(TvSeries tvSeries, List<TvSeriesGenre> genres, List<TvSeriesEpisode> episodes, List<TvSeriesPicture> pictures) {
        this.tvSeries = tvSeries;
        this.genres = genres;
        this.episodes = episodes;
        this.pictures = pictures;
    }

    public TvSeries getTvSeries() {
        return tvSeries;
    }

    public void setTvSeries(TvSeries tvSeries) {
        this.tvSeries = tvSeries;
    }

    public List<TvSeriesGenre> getGenres() {
        return genres;
    }

    public void setGenres(List<TvSeriesGenre> genres) {
        this.genres = genres;
    }

    public List<TvSeriesEpisode> getEpisodes() {
        return episodes;
    }

    public void setEpisodes(List<TvSeriesEpisode> episodes) {
        this.episodes = episodes;
    }

    public List<TvSeriesPicture> getPictures() {
        return pictures;
    }

    public void setPictures(List<TvSeriesPicture> pictures) {
        this.pictures = pictures;
    }
}
