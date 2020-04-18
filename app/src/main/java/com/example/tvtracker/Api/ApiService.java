package com.example.tvtracker.Api;

import com.example.tvtracker.JsonModels.TvShowBasic.JsonTvShowBasicRoot;
import com.example.tvtracker.JsonModels.TvShowDetails.JsonTvShowDetailsRoot;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {

    @GET("most-popular")
    Call<JsonTvShowBasicRoot> getTvShowsBasic();

    @GET("https://www.episodate.com/api/show-details?")
    Call<JsonTvShowDetailsRoot> getTvShowDetailed(@Query("tvShowId") int tvShowId);


}
