package com.example.tvtracker;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.tvtracker.Models.TvShow;
import com.example.tvtracker.ViewModels.TvShowViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TvShowViewModel tvShowViewModel;

    public static final String TVSHOW_WATCHING_FLAG_YES = "YES";
    public static final String TVSHOW_BUNDLE = "tv_show_bundle";
    public static final String TVSHOW_ID = "tv_show_id";
    public static final String TVSHOW_NAME = "tv_show_name";
    public static final String TVSHOW_STATUS = "tv_show_status";
    public static final String TVSHOW_START_DATE = "tv_show_start_date";
    public static final String TVSHOW_END_DATE = "tv_show_end_date";
    public static final String TVSHOW_DESCRIPTION = "tv_show_description";
    public static final String TVSHOW_YOUTUBE_LINK = "tv_show_youtube_link";
    public static final String TVSHOW_RATING = "tv_show_rating";
    public static final String TVSHOW_IMAGE_PATH = "tv_show_image_path";
    public static final String TVSHOW_COUNTRY = "tv_show_country";
    public static final String TVSHOW_NETWORK = "tv_show_network";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        tvShowViewModel = new ViewModelProvider(this).get(TvShowViewModel.class);


        BottomNavigationView navView = findViewById(R.id.bottom_navigation);
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_container);
        NavigationUI.setupWithNavController(navView, navController);

        final RelativeLayout relativeLayout = findViewById(R.id.syncState);
        final ConstraintLayout constraintLayout = findViewById(R.id.appState);


//        constraintLayout.setVisibility(View.GONE);
//        relativeLayout.setVisibility(View.VISIBLE);


//        tvShowViewModel.allInOne();


//        relativeLayout.setVisibility(View.GONE);
//        constraintLayout.setVisibility(View.VISIBLE);


    }

    public static Bundle toBundle(TvShow tvShow){
        Bundle newBundle = new Bundle();
        newBundle.putString(MainActivity.TVSHOW_NAME, tvShow.getTvShowName());
        newBundle.putString(MainActivity.TVSHOW_ID, String.valueOf(tvShow.getTvShowId()));
        newBundle.putString(MainActivity.TVSHOW_STATUS, tvShow.getTvShowStatus());
        newBundle.putString(MainActivity.TVSHOW_DESCRIPTION, tvShow.getTvShowDesc());
        newBundle.putString(MainActivity.TVSHOW_YOUTUBE_LINK, tvShow.getTvShowYoutubeLink());
        newBundle.putString(MainActivity.TVSHOW_RATING, tvShow.getTvShowRating());
        newBundle.putString(MainActivity.TVSHOW_IMAGE_PATH, tvShow.getTvShowImagePath());
        newBundle.putString(MainActivity.TVSHOW_COUNTRY, tvShow.getTvShowCountry());
        newBundle.putString(MainActivity.TVSHOW_NETWORK, tvShow.getTvShowNetwork());
        newBundle.putString(MainActivity.TVSHOW_START_DATE, tvShow.getTvShowStartDate());
        newBundle.putString(MainActivity.TVSHOW_END_DATE, tvShow.getTvShowEndDate());

        return newBundle;
    }



}
