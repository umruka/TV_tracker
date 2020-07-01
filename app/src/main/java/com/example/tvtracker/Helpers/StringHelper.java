package com.example.tvtracker.Helpers;

import com.example.tvtracker.Models.TvShowGenre;

import java.util.List;

public class StringHelper {
    public static String addZero(int count){
        return count < 10 ? "0" + count : String.valueOf(count);
    }

    public static String getGenresString(List<TvShowGenre> genres) {
        String genresString = "";
        for (TvShowGenre genre : genres){
            genresString +=  genre.equals(genres.get(genres.size()  - 1)) ? genre.getGenreName() : genre.getGenreName() + ", ";
        }
        return genresString;
    }

    public static String getTvShowRatingString(String rating)  {
        return rating.length() >= 5 ?  rating.substring(0, 4) : rating;
    }

}
