package com.example.tvtracker.Repository;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.tvtracker.TvShowModel.TvShow;

import java.util.List;

@Dao
public interface TvShowDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTVShow(TvShow tvShow);

    @Query("DELETE FROM tv_show_table")
    void deleteAllTvShows();

    @Query("DELETE FROM tv_show_table WHERE tv_show_id IN (:id)")
    void deleteTvShowById(int id);

    @Query("UPDATE tv_show_table SET tv_show_name=:name, tv_show_status=:status WHERE tv_show_id IN(:tvShowId)")
    void updateTvShow(int tvShowId, String name, String status);

    @Query("UPDATE tv_show_table SET tv_show_flag=:watchingId WHERE tv_show_id IN(:id)")
    void updateTvShowWatchingFlag(int id, String watchingId);

    @Query("SELECT * FROM tv_show_table WHERE tv_show_id=:Id")
    TvShow getTvShowById(int Id);

    @Query("SELECT * FROM tv_show_table")
    LiveData<List<TvShow>> getAllTvShows();

    @Query("SELECT * FROM tv_show_table WHERE tv_show_flag=:flag")
    LiveData<List<TvShow>> getWatchlistTvShows(String flag);
}
