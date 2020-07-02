package com.watermelon.Repository;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.watermelon.Models.TvSeriesCalendarEpisode;
import com.watermelon.Models.TvSeries;
import com.watermelon.Models.TvSeriesEpisode;
import com.watermelon.Models.TvSeriesFull;
import com.watermelon.Models.TvSeriesGenre;
import com.watermelon.Models.TvSeriesPicture;

import java.util.List;

@Dao
public interface AppDao {

    //Watchlist
    @Transaction
    @Query("SELECT * FROM tv_series_table WHERE tv_series_flag=:flag")
    LiveData<List<TvSeriesFull>> getWatchlistTvSeriesFull(boolean flag);

    //Calendar
    @Transaction
    @Query("SELECT * FROM tv_series_table, tv_series_episode_table WHERE tv_series_flag=:flag AND tv_series_api_id = episode_tv_series_id AND date(episode_air_date)>=date('now') AND date(episode_air_date) <= date('now', '+14 days') ORDER BY date(episode_air_date) ASC")
    LiveData<List<TvSeriesCalendarEpisode>> getCalendarTvSeries(boolean flag);


    //Discover
    @Query("SELECT * FROM tv_series_table")
    LiveData<List<TvSeries>> getDiscoverTvSeries();

    //Statistics
    @Transaction
    @Query("SELECT * FROM tv_series_table WHERE tv_series_flag=:flag")
    List<TvSeriesFull> getStatisticsTvSeriesFull(boolean flag);

    //TvSeries
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertTvSeries(TvSeries tvSeries);

    @Query("UPDATE tv_series_table SET tv_series_name=:name, tv_series_status=:status, tv_series_start_date=:startDate, tv_series_end_date=:endDate, tv_series_country=:country, tv_series_network=:network, tv_series_image_path=:imagePath WHERE tv_series_api_id IN(:id)")
    void updateTvSeries(int id, String name, String status, String startDate, String endDate, String country, String network, String imagePath);

    @Query("DELETE FROM tv_series_table")
    void deleteAllTvSeries();

    @Query("DELETE FROM tv_series_table WHERE tv_series_api_id IN (:id)")
    void deleteTvSeriesById(int id);

    @Query("UPDATE tv_series_table SET tv_series_description=:description, tv_series_runtime=:runtime, tv_series_youtube_link=:youtubeLink, tv_series_rating=:rating WHERE tv_series_api_id IN(:tvSeriesId)")
    void updateTvSeriesDetails(int tvSeriesId, String description, String runtime, String youtubeLink, String rating);

    @Query("UPDATE tv_series_table SET tv_series_flag=:watchingId WHERE tv_series_api_id IN(:id)")
    void updateTvSeriesWatchedFlag(int id, boolean watchingId);


    @Query("SELECT * FROM tv_series_table WHERE tv_series_id=:Id")
    TvSeries getTvSeriesById(int Id);

    @Transaction
    @Query("SELECT * FROM tv_series_table WHERE tv_series_api_id=:Id")
    TvSeriesFull getTvSeriesByApiId(int Id);

    @Query("SELECT tv_series_api_id FROM tv_series_table WHERE tv_series_api_id IN(:ids)")
    List<Integer> getTvSeriesIfExists(List<Integer> ids);

    //TvSeriesFull
    @Transaction
    @Query("SELECT * FROM tv_series_table WHERE tv_series_api_id=:id")
    LiveData<TvSeriesFull> getTvSeriesFullById(int id);

    //TvSeriesPictures
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertAllTvSeriesPictures(List<TvSeriesPicture> pictures);

    //TvSeriesEpisode
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTvSeriesEpisode(TvSeriesEpisode tvSeriesEpisode);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertAllTvSeriesEpisodes(List<TvSeriesEpisode> episodes);

    @Query("UPDATE tv_series_episode_table SET episode_is_watched=:isWatched WHERE id IN (:id)")
    void updateTvSeriesEpisodeWatchedFlag(int id, boolean isWatched);

    @Query("UPDATE tv_series_episode_table SET episode_is_watched=:flag WHERE id IN (:ids)")
    void updateTvSeriesAllSeasonWatched(List<Integer> ids, boolean flag);

    @Query("SELECT * FROM tv_series_episode_table WHERE episode_tv_series_id IN (:tvSeriesId) AND episode_season IN(:seasonNum)")
    List<TvSeriesEpisode> getTvSeriesEpisodesByIdAndSeasonNum(int tvSeriesId, int seasonNum);

    @Query("SELECT MAX(episode_air_date) FROM tv_series_episode_table WHERE episode_tv_series_id=:id")
    String getDateForTheLastEpisodeOfTvSeriesAired(int id);

    //TvSeriesGenre
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertAllTvSeriesGenres(List<TvSeriesGenre> genres);


}
