package com.example.tvtracker.Repository;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.tvtracker.Models.TvShowCombined;
import com.example.tvtracker.Models.TvShowDetails;
import com.example.tvtracker.Models.TvShowBasic;

import java.util.List;

@Dao
public interface AppDao {


    //TvShowBasic
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTvShowFull(TvShowDetails tvShowDetails);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTVShow(TvShowBasic tvShowBasic);

    @Query("DELETE FROM tv_show_basic_table")
    void deleteAllTvShows();

    @Query("DELETE FROM tv_show_basic_table WHERE tv_show_id IN (:id)")
    void deleteTvShowById(int id);

    @Query("UPDATE tv_show_basic_table SET tv_show_name=:name, tv_show_status=:status WHERE tv_show_id IN(:tvShowId)")
    void updateTvShow(int tvShowId, String name, String status);

    @Query("UPDATE tv_show_basic_table SET tv_show_flag=:watchingId WHERE tv_show_id IN(:id)")
    void updateTvShowWatchingFlag(int id, String watchingId);

    @Query("SELECT * FROM tv_show_basic_table WHERE tv_show_id=:Id")
    TvShowBasic getTvShowById(int Id);

    @Query("SELECT * FROM tv_show_basic_table")
    LiveData<List<TvShowBasic>> getAllTvShows();


    @Query("SELECT * FROM tv_show_basic_table WHERE tv_show_flag=:flag")
    LiveData<List<TvShowBasic>> getWatchlistTvShows(String flag);

    //TvShowDetails
    @Query("SELECT * FROM tv_show_details_table")
    LiveData<List<TvShowDetails>> getAllTvShowsFull();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTVShowDetailed(TvShowDetails tvShowShort);

    @Query("DELETE FROM tv_show_details_table")
    void deleteAllTvShowsDetailed();

    @Query("DELETE FROM tv_show_details_table WHERE tv_show_id IN (:id)")
    void deleteTvShowDetailedById(int id);


    @Query("SELECT * FROM tv_show_details_table WHERE tv_show_id=:Id")
    TvShowDetails getTvShowDetailedById(int Id);

    //TvShowCombined
    @Transaction
    @Query("SELECT * FROM tv_show_basic_table WHERE tv_show_flag=:flag")
    LiveData<List<TvShowCombined>> getAllTvShowsCombined(String flag);


}
