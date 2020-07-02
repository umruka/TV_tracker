package com.watermelon.Repository;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.watermelon.Models.CalendarTvShowEpisode;
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
    LiveData<List<TvSeriesFull>> getWatchlistTvShowsFull(boolean flag);

    //Calendar
    @Transaction
    @Query("SELECT * FROM tv_series_table, tv_series_episode_table WHERE tv_series_flag=:flag AND tv_series_api_id = episode_tv_show_id AND date(episode_air_date)>=date('now') AND date(episode_air_date) <= date('now', '+14 days') ORDER BY date(episode_air_date) ASC")
    LiveData<List<CalendarTvShowEpisode>> getCalendarTvShows(boolean flag);


    //Discover
    @Query("SELECT * FROM tv_series_table")
    LiveData<List<TvSeries>> getDiscoverTvShows();

    //Statistics
    @Transaction
    @Query("SELECT * FROM tv_series_table WHERE tv_series_flag=:flag")
    List<TvSeriesFull> getStatisticsTvShowsFull(boolean flag);

    //TvSeries
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertTvShow(TvSeries tvSeries);

    @Query("UPDATE tv_series_table SET tv_series_name=:name, tv_series_status=:status, tv_series_start_date=:startDate, tv_series_end_date=:endDate, tv_series_country=:country, tv_series_network=:network, tv_series_image_path=:imagePath WHERE tv_series_api_id IN(:id)")
    void updateTvShow(int id, String name, String status, String startDate, String endDate, String country, String network, String imagePath);

    @Query("DELETE FROM tv_series_table")
    void deleteAllTvShows();

    @Query("DELETE FROM tv_series_table WHERE tv_series_api_id IN (:id)")
    void deleteTvShowById(int id);

    @Query("UPDATE tv_series_table SET tv_series_description=:description, tv_series_runtime=:runtime, tv_series_youtube_link=:youtubeLink, tv_series_rating=:rating WHERE tv_series_api_id IN(:tvShowId)")
    void updateTvSeriesDetails(int tvShowId, String description, String runtime, String youtubeLink, String rating);

    @Query("UPDATE tv_series_table SET tv_series_flag=:watchingId WHERE tv_series_api_id IN(:id)")
    void updateTvShowWatchedFlag(int id, boolean watchingId);


    @Query("SELECT * FROM tv_series_table WHERE tv_series_id=:Id")
    TvSeries getTvShowById(int Id);

    @Transaction
    @Query("SELECT * FROM tv_series_table WHERE tv_series_api_id=:Id")
    TvSeriesFull getTvShowByApiId(int Id);

    @Query("SELECT tv_series_api_id FROM tv_series_table WHERE tv_series_api_id IN(:ids)")
    List<Integer> getTvShowsIfExists(List<Integer> ids);

    //TvSeriesFull
    @Transaction
    @Query("SELECT * FROM tv_series_table WHERE tv_series_api_id=:id")
    LiveData<TvSeriesFull> getTvShowFullById(int id);

    //TvShowPictures
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertAllTvShowPictures(List<TvSeriesPicture> pictures);

    //TvSeriesEpisode
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTvShowEpisode(TvSeriesEpisode tvSeriesEpisode);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertAllTvShowEpisodes(List<TvSeriesEpisode> episodes);

    @Query("UPDATE tv_series_episode_table SET episode_is_watched=:isWatched WHERE id IN (:id)")
    void updateTvSeriesEpisodeWatchedFlag(int id, boolean isWatched);

    @Query("UPDATE tv_series_episode_table SET episode_is_watched=:flag WHERE id IN (:ids)")
    void updateTvSeriesAllSeasonWatched(List<Integer> ids, boolean flag);

    @Query("SELECT * FROM tv_series_episode_table WHERE episode_tv_show_id IN (:tvShowId) AND episode_season IN(:seasonNum)")
    List<TvSeriesEpisode> getTvShowEpisodesByIdAndSeasonNum(int tvShowId, int seasonNum);

    @Query("SELECT MAX(episode_air_date) FROM tv_series_episode_table WHERE episode_tv_show_id=:id")
    String getDateForTheLastEpisodeOfTvShowAired(int id);

    //TvSeriesGenre
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertAllTvShowGenres(List<TvSeriesGenre> genres);


}
