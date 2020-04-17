package com.example.tvtracker.Repository;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.tvtracker.TvShowFullModel.TvShowFull;
import com.example.tvtracker.TvShowModel.TvShow;

@Database(entities = {TvShow.class, TvShowFull.class}, version = 5)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase instance;

    public abstract AppDao appDao();

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    AppDatabase.class, "tv_show_database")
                    .fallbackToDestructiveMigration()
                    .build();

        }
        return instance;
    }
}
