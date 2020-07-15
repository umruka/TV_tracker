package com.watermelon.UI;

import android.os.Bundle;
import android.view.View;

import com.watermelon.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

public class WatermelonActivity extends AppCompatActivity implements NavController.OnDestinationChangedListener {

    public static final boolean TEST_MODE =  false;

    public static final String NETWORK_DEFAULT = "";
    public static final String NETWORK_CW = "The CW";
    public static final String NETWORK_NETFLIX = "Netflix";
    public static final String NETWORK_HBO = "HBO";
    public static final String NETWORK_AMC = "AMC";
    public static final String NETWORK_FOX = "FOX";

    public static final String STATUS_RUNNING = "Running";
    public static final String STATUS_ENDED = "Ended";

    public static final int NETWORKS_CODE = 3;
    public static final int STATUS_CODE = 4;

    public static final boolean TVSERIES_WATCHED_FLAG_YES = true;
    public static final boolean TVSERIES_WATCHED_FLAG_NO = false;
    public static final boolean TVSERIES_WATCHED_EPISODE_FLAG_YES = true;
    public static final boolean TVSERIES_WATCHED_EPISODE_FLAG_NO = false;
    public static final String TVSERIES_ID = "tv_series_id";
    public static final String TVSERIES_SEASON_NUM = "tv_series_season_number";
    public static final int TV_SERIES_MOST_POPULAR_PAGES_COUNT = 5;

    private BottomNavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navigationView = findViewById(R.id.bottom_navigation);
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_container);
        NavigationUI.setupWithNavController(navigationView, navController);

        navController.addOnDestinationChangedListener(this);

    }

    @Override
    public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
        if (navigationView.getVisibility()  == View.GONE) {
            navigationView.setVisibility(View.VISIBLE);
        }
        switch (destination.getId()) {
            case R.id.fragment_details:
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                break;
            case R.id.fragment_search:
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                navigationView.setVisibility(View.GONE);
                break;
            case R.id.navigation_watchlist:
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                break;
            case R.id.navigation_calendar:
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                break;
            case R.id.navigation_discover:
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                break;
            case R.id.navigation_statistics:
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                break;
            default:
                break;
        }
    }

}
