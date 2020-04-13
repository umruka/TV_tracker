package com.example.tvtracker.movies;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tvtracker.R;

import java.util.ArrayList;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieHolder> {
    private List<Movie> movies = new ArrayList<>();
    @NonNull
    @Override
    public MovieHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_item, parent, false);
        return new MovieHolder(itemView);
    }

    public List<Movie> setMovies(List<Movie> movies){
        this.movies = movies;
        return movies;
    }

    @Override
    public void onBindViewHolder(@NonNull MovieHolder holder, int position) {
    Movie movie = movies.get(position);
    holder.textViewMovieTitle.setText(movie.getTitle());
    holder.textViewMovieGenre.setText(movie.getGenre());
    holder.textViewMovieIMDBrating.setText(String.valueOf(movie.getImdbRating()));
    holder.textViewMovieReleaseYear.setText(String.valueOf(movie.getReleaseDate()));
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }
}
