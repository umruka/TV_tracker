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
    void insertTvShowBasic(TvShowBasic tvShowBasic);

    @Query("DELETE FROM tv_show_basic_table")
    void deleteAllTvShowsBasic();

    @Query("DELETE FROM tv_show_basic_table WHERE tv_show_id IN (:id)")
    void deleteTvShowBasicById(int id);

    @Query("UPDATE tv_show_basic_table SET tv_show_name=:name, tv_show_status=:status WHERE tv_show_id IN(:tvShowId)")
    void updateTvShowBasic(int tvShowId, String name, String status);

    @Query("UPDATE tv_show_basic_table SET tv_show_flag=:watchingId WHERE tv_show_id IN(:id)")
    void updateTvShowBasicWatchingFlag(int id, String watchingId);

    @Query("SELECT * FROM tv_show_basic_table WHERE tv_show_id=:Id")
    TvShowBasic getTvShowBasicById(int Id);

    @Query("SELECT * FROM tv_show_basic_table")
    LiveData<List<TvShowBasic>> getAllTvShowsBasic();

    //Watchlist
    @Query("SELECT * FROM tv_show_basic_table WHERE tv_show_flag=:flag")
    LiveData<List<TvShowBasic>> getWatchlistTvShowsBasic(String flag);

    //TvShowDetails

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTvShowDetails(TvShowDetails tvShowDetails);

    @Query("SELECT * FROM tv_show_details_table")
    LiveData<List<TvShowDetails>> getAllTvShowsDetails();

    @Query("DELETE FROM tv_show_details_table")
    void deleteAllTvShowsDetails();

    @Query("DELETE FROM tv_show_details_table WHERE tv_show_id IN (:id)")
    void deleteTvShowDetailsById(int id);

    @Query("UPDATE tv_show_details_table SET tv_show_description=:description, tv_show_youtube_link=:youtubeLink, tv_show_rating=:rating, tv_show_image_path=:imagePath")
    void updateTvShowDetails(String description, String youtubeLink, String rating, String imagePath);

    @Query("SELECT * FROM tv_show_details_table WHERE tv_show_id=:Id")
    TvShowDetails getTvShowDetailsById(int Id);

    //TvShowCombined
    @Transaction
    @Query("SELECT * FROM tv_show_basic_table WHERE tv_show_flag=:flag")
    LiveData<List<TvShowCombined>> getAllTvShowsCombined(String flag);


}
