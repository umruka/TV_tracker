package com.example.tvtracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import com.example.tvtracker.Models.TvShow;
import com.example.tvtracker.ViewModels.TvShowViewModel;

import java.util.List;

public class SyncActivity extends AppCompatActivity {

    private TvShowViewModel tvShowViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sync);

        tvShowViewModel = new ViewModelProvider(this).get(TvShowViewModel.class);
//        tvShowViewModel.getAllTvShows().observe(this, new Observer<List<TvShow>>() {
//            @Override
//            public void onChanged(List<TvShow> tvShows) {
//
//            }
//        });


//        tvShowViewModel.allInOne();



    }
}
