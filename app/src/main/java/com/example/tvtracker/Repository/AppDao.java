package com.example.tvtracker.Repository;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.tvtracker.Models.QueryModels.fromDbCall;
import com.example.tvtracker.Models.TvShow;
import com.example.tvtracker.Models.TvShowEpisode;
import com.example.tvtracker.Models.TvShowGenre;
import com.example.tvtracker.Models.TvShowPicture;

import java.util.List;

@Dao
public interface AppDao {


    //TvShow

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertTvShows(List<TvShow> tvShows);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insertTvShow(TvShow tvShow);

//    @Insert(onConflict = OnConflictStrategy.IGNORE)
//    long updateTvShow(TvShow tvShow);

    @Query("UPDATE tv_show_table SET tv_show_name=:name, tv_show_status=:status WHERE tv_show_id IN(:id)")
    int updateTvShow(int id, String name, String status);

    @Query("DELETE FROM tv_show_table")
    void deleteAllTvShows();

    @Query("DELETE FROM tv_show_table WHERE tv_show_id IN (:id)")
    void deleteTvShowById(int id);


    @Query("UPDATE tv_show_table SET tv_show_description=:description, tv_show_youtube_link=:youtubeLink, tv_show_rating=:rating WHERE tv_show_id IN(:tvShowId)")
    void updateTvShowDetails(int tvShowId, String description, String youtubeLink, String rating);

    @Query("UPDATE tv_show_table SET tv_show_flag=:watchingId WHERE tv_show_id IN(:id)")
    void updateTvShowWatchingFlag(int id, String watchingId);



    @Query("SELECT * FROM tv_show_table WHERE tv_show_id=:Id")
    TvShow getTvShowById(int Id);

    @Query("SELECT * FROM tv_show_table")
    List<TvShow> getAllTvShows();

    //Watchlist
    @Query("SELECT * FROM tv_show_table WHERE tv_show_flag=:flag")
    List<TvShow> getWatchlistTvShows(String flag);

    @Query("SELECT * FROM tv_show_table WHERE tv_show_flag=:flag")
    List<TvShow> getWatchlistListTvShows(String flag);

    //TvShowPicture

    @Insert(onConflict =  OnConflictStrategy.REPLACE)
    void insertTvShowPicture(TvShowPicture tvShowPicture);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertAllTvShowPictures(List<TvShowPicture> pictures);

    @Query("SELECT * FROM tv_show_picture_table WHERE tv_show_id IN (:tvShowId)")
    List<TvShowPicture>  getTvShowPicturesByTvShowId(int tvShowId);


    //TvShowEpisode
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTvShowEpisode(TvShowEpisode tvShowEpisode);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertAllTvShowEpisodes(List<TvShowEpisode> episodes);

    @Query("UPDATE tv_show_episode_table SET tv_show_is_watched=:isWatched WHERE id IN (:id)")
    void updateTvShowEpisodeWatchedFlag(int id, boolean isWatched);

    @Query("SELECT * FROM tv_show_episode_table WHERE tv_show_id IN (:tvShowId)")
    List<TvShowEpisode> getTvShowEpisodesById(int tvShowId);


    @Query("SELECT * FROM tv_show_episode_table WHERE tv_show_id IN (:tvShowId) AND tv_show_season IN(:seasonNum)")
    List<TvShowEpisode> getTvShowEpisodesByIdAndSeasonNum(int tvShowId, int seasonNum);

    @Query("SELECT MAX(tv_show_season) FROM tv_show_episode_table WHERE tv_show_id IN(:tvShowId)")
    int getMaxSeasonByTvShowId(int tvShowId);

    //TvShowGenre

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTvShowGenre(TvShowGenre tvShowGenre);


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertAllTvShowGenres(List<TvShowGenre> genres);

    @Query("SELECT * FROM tv_show_genre_table WHERE tv_show_id IN (:tvShowId)")
    List<TvShowGenre> getTvShowGenresById(int tvShowId);


    //DetailsFragment
    @Transaction
    @Query("SELECT * FROM tv_show_table WHERE tv_show_id IN (:tvShowId)")
    List<fromDbCall> getTvShowWithPicturesAndEpisodesById(int tvShowId);


}
