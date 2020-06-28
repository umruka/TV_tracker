package com.example.tvtracker;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toolbar;

import com.example.tvtracker.UI.Discover.DiscoverViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends AppCompatActivity implements NavController.OnDestinationChangedListener {

    private DiscoverViewModel discoverViewModel;

    public static final boolean TEST_MODE =  false;


    public static final boolean TVSHOW_WATCHING_FLAG_YES = true;
    public static final boolean TVSHOW_WATCHING_FLAG_NO = false;
    public static final String TVSHOW_ID = "tv_show_id";
    public static final String TVSHOW_SEASON_NUM = "tv_show_season_number";
    public static final boolean TVSHOW_WATCHED_EPISODE_FLAG_YES = true;
    public static final boolean TVSHOW_WATCHED_EPISODE_FLAG_NO = false;
    public static final int TV_SHOW_MOST_POPULAR_PAGES_COUNT = 5;

    private BottomNavigationView navigationView;
    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        discoverViewModel = new ViewModelProvider(this).get(DiscoverViewModel.class);
        navigationView = findViewById(R.id.bottom_navigation);
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_container);
        NavigationUI.setupWithNavController(navigationView, navController);

        navController.addOnDestinationChangedListener(this);

        final RelativeLayout relativeLayout = findViewById(R.id.syncState);
        final ConstraintLayout constraintLayout = findViewById(R.id.appState);


//        constraintLayout.setVisibility(View.GONE);
//        relativeLayout.setVisibility(View.VISIBLE);


//        discoverViewModel.allInOne();


//        relativeLayout.setVisibility(View.GONE);
//        constraintLayout.setVisibility(View.VISIBLE);


    }

    @Override
    public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
        switch (destination.getId()) {
            case R.id.fragment_details:
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                break;
            case R.id.fragment_search:
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
            case R.id.navigation_profile:
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                break;
            default:
                navigationView.setVisibility(View.VISIBLE);
        }
    }
}
