package com.example.tvtracker.Api;

import com.example.tvtracker.JsonModels.JsonTvShowSearchRoot;
import com.example.tvtracker.JsonModels.TvShowBasicInfo.JsonTvShowBasicInfoRoot;
import com.example.tvtracker.JsonModels.TvShowDetails.JsonTvShowDetailsInfoRoot;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {

    @GET("most-popular")
    Call<JsonTvShowBasicInfoRoot> getTvShowsBasic();

    @GET("show-details")
    Call<JsonTvShowDetailsInfoRoot> getTvShowDetailed(@Query("q") int tvShowId);

    @GET("search")
    Call<JsonTvShowSearchRoot> getTvShowSearch(@Query("q") String searchShow, @Query("page") int pageNum);
}
