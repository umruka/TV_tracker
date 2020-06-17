package com.example.tvtracker;

import android.os.Bundle;
import android.widget.RelativeLayout;

import com.example.tvtracker.Models.TvShow;
import com.example.tvtracker.ViewModels.DiscoverViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends AppCompatActivity {

    private DiscoverViewModel discoverViewModel;

    public static final boolean TVSHOW_WATCHING_FLAG_YES = true;
    public static final boolean TVSHOW_WATCHING_FLAG_NO = false;
    public static final String TVSHOW_ID = "tv_show_id";
    public static final String TVSHOW_SEASON_NUM = "tv_show_season_number";
    public static final boolean TVSHOW_WATCHED_EPISODE_FLAG_YES = true;
    public static final boolean TVSHOW_WATCHED_EPISODE_FLAG_NO = false;
    public static final int TV_SHOW_MOST_POPULAR_PAGES_COUNT = 5;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        discoverViewModel = new ViewModelProvider(this).get(DiscoverViewModel.class);


        BottomNavigationView navView = findViewById(R.id.bottom_navigation);
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_container);
        NavigationUI.setupWithNavController(navView, navController);

        final RelativeLayout relativeLayout = findViewById(R.id.syncState);
        final ConstraintLayout constraintLayout = findViewById(R.id.appState);


//        constraintLayout.setVisibility(View.GONE);
//        relativeLayout.setVisibility(View.VISIBLE);


//        discoverViewModel.allInOne();


//        relativeLayout.setVisibility(View.GONE);
//        constraintLayout.setVisibility(View.VISIBLE);


    }




}
