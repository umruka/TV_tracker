package com.example.tvtracker.movies;

import java.util.Date;

public class Movie {
    private String title;
    private String genre;
    private int imdbRating;
    private int releaseYear;

    public Movie(String title, String genre, int imdbRating, int releaseDate) {
        this.title = title;
        this.genre = genre;
        this.imdbRating = imdbRating;
        this.releaseYear = releaseDate;
    }
//private String subGenre;
    //trailer
    //cast
    //flag watched
    //logo

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public int getImdbRating() {
        return imdbRating;
    }

    public void setImdbRating(int imdbRating) {
        this.imdbRating = imdbRating;
    }

    public int getReleaseDate() {
        return releaseYear;
    }

    public void setReleaseDate(int releaseDate) {
        this.releaseYear = releaseDate;
    }
}
