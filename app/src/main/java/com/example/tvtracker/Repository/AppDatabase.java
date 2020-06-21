package com.example.tvtracker.Repository;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.tvtracker.Models.TvShow;
import com.example.tvtracker.Models.TvShowEpisode;
import com.example.tvtracker.Models.TvShowGenre;
import com.example.tvtracker.Models.TvShowPicture;


@Database(entities = {TvShow.class, TvShowEpisode.class, TvShowPicture.class, TvShowGenre.class}, version = 4)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase instance;
    public abstract AppDao appDao();

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    AppDatabase.class, "tv_tracker_db")
                    .fallbackToDestructiveMigration()
                    .build();

        }
        return instance;
    }
}
