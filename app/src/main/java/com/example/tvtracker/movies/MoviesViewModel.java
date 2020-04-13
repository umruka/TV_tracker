package com.example.tvtracker.movies;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class MoviesViewModel extends AndroidViewModel {
    // TODO: Implement the ViewModel
    MoviesRepository repository;
    //LiveData<Movie> allMovies;
    List<Movie> allMovies;
    public MoviesViewModel(@NonNull Application application) {
        super(application);
        this.allMovies = repository.getAllMovies();
    }
}
