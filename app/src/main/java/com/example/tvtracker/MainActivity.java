package com.example.tvtracker;

import android.os.Bundle;

import com.example.tvtracker.movies.MoviesFragment;
import com.example.tvtracker.profile.profileFragment;
import com.example.tvtracker.tvSeries.tvSeriesFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.MenuItem;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {


        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_tvseries:
                    tvSeriesFragment tvSeriesFragment = new tvSeriesFragment();
                    changeFragment(tvSeriesFragment);
                    return true;
                case R.id.navigation_movies:
                    MoviesFragment moviesFragment = new MoviesFragment();
                    changeFragment(moviesFragment);
                    return true;
                case R.id.navigation_search:
                    return true;
                case R.id.navigation_profile:
                    profileFragment profileFragment = new profileFragment();
                    changeFragment(profileFragment);
                    return true;
            }
            return false;
        }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.bottom_navigation);
        navView.setOnNavigationItemSelectedListener(this);







    }

    private void changeFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.fragment_container,fragment);
        fragmentTransaction.commit();
    }

}
