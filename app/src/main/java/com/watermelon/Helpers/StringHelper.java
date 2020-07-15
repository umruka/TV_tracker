package com.watermelon.Helpers;

import com.watermelon.Models.TvSeriesGenre;

import java.util.List;

public class StringHelper {
    public static String addZero(int count){
        return count < 10 ? "0" + count : String.valueOf(count);
    }

    public static String getGenresString(List<TvSeriesGenre> genres) {
        String genresString = "";
        for (TvSeriesGenre genre : genres){
            genresString +=  genre.equals(genres.get(genres.size()  - 1)) ? genre.getGenreName() : genre.getGenreName() + ", ";
        }
        return genresString;
    }

    public static String getTvSeriesRatingString(String rating)  {
        return rating.length() >= 5 ?  rating.substring(0, 4) : rating;
    }

}
