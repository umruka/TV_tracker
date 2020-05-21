package com.example.tvtracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import com.example.tvtracker.ViewModels.DiscoverViewModel;

public class SyncActivity extends AppCompatActivity {

    private DiscoverViewModel discoverViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sync);

        discoverViewModel = new ViewModelProvider(this).get(DiscoverViewModel.class);
//        discoverViewModel.getAllTvShows().observe(this, new Observer<List<TvShow>>() {
//            @Override
//            public void onChanged(List<TvShow> tvShows) {
//
//            }
//        });


//        discoverViewModel.allInOne();



    }
}
