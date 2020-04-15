package com.example.tvtracker.Repository;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.tvtracker.WatchlistModel.Watchlist;

import java.util.List;
@Dao
public interface WatchListDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertWatchlistItem(Watchlist watchlist);

    @Query("SELECT * FROM watchlist_table")
    LiveData<List<Watchlist>> getAllWatchlist();


}
