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

    @Query("DELETE FROM tvshow_table")
    void deleteAllTvShows();

    @Query("DELETE FROM tvshow_table WHERE tvShowId IN (:id)")
    void deleteTvShowById(int id);

    @Query("UPDATE tvshow_table SET tvShowName=:name, tvShowStatus=:status WHERE tvShowId IN(:tvShowId)")
    void updateTvShow(int tvShowId, String name, String status);

    @Query("SELECT * FROM tvshow_table WHERE tvShowId=:Id")
    TvShow getTvShowById(int Id);

    @Query("SELECT * FROM tvshow_table")
    LiveData<List<TvShow>> getAllTvShows();
}
