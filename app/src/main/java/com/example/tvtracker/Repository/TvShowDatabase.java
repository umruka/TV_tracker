package com.example.tvtracker.Repository;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.tvtracker.TvShowModel.TvShow;

@Database(entities = {TvShow.class},version = 3)
public abstract class TvShowDatabase extends RoomDatabase {

    private static TvShowDatabase instance;

    public abstract TvShowDao tv_showDao();

    public static synchronized TvShowDatabase getInstance(Context context){
        if(instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    TvShowDatabase.class, "tvshow_database")
                    .fallbackToDestructiveMigration()
                    .build();

        }
        return instance;
    }
}
