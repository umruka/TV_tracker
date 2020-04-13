package com.example.tvtracker.movies;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tvtracker.R;

public class MovieHolder extends RecyclerView.ViewHolder {
public TextView textViewMovieTitle;
public TextView textViewMovieGenre;
public TextView textViewMovieIMDBrating;
public TextView textViewMovieReleaseYear;

    public MovieHolder(@NonNull View itemView) {
        super(itemView);
        textViewMovieTitle = itemView.findViewById(R.id.text_view_movie_title);
        textViewMovieGenre = itemView.findViewById(R.id.text_view_movie_genre);
        textViewMovieReleaseYear = itemView.findViewById(R.id.text_view_movie_year);
        textViewMovieIMDBrating = itemView.findViewById(R.id.text_view_movie_imdb_rating);


    }
}
