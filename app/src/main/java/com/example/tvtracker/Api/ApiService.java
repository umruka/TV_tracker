package com.example.tvtracker.Api;

import com.example.tvtracker.JsonModels.TvShowBasic.JsonTvShowBasicRoot;
import com.example.tvtracker.JsonModels.TvShowDetails.JsonTvShowDetails;
import com.example.tvtracker.JsonModels.TvShowDetails.JsonTvShowDetailsRoot;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {

    @GET("most-popular")
    Call<JsonTvShowBasicRoot> getTvShowsBasic();

    @GET("show-details")
    Call<JsonTvShowDetailsRoot> getTvShowDetailed(@Query("q") int tvShowId);


}
