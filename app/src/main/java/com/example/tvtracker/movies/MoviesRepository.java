package com.example.tvtracker.movies;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.ArrayList;
import java.util.List;

public class MoviesRepository {
    //LiveData<List<Movie>> allMovies;
    List<Movie> allMovies;
    public MoviesRepository (){
    List<Movie> movies = new ArrayList<>();
    allMovies = movies;
    }

    public List<Movie> getAllMovies() {
        return allMovies;
    }
}
