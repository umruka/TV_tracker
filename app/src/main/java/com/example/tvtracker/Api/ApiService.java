package com.example.tvtracker.Api;

import com.example.tvtracker.JsonModels.JsonTvShowSearchRoot;
import com.example.tvtracker.JsonModels.TvShowBasicInfo.JsonTvShowBasicRoot;
import com.example.tvtracker.JsonModels.TvShowDetails.JsonTvShowFullRoot;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {

    @GET("most-popular")
    Call<JsonTvShowBasicRoot> getTvShowsBasic(@Query("page") int page);

    @GET("show-details")
    Call<JsonTvShowFullRoot> getTvShowDetailed(@Query("q") int tvShowId);

    @GET("search")
    Call<JsonTvShowSearchRoot> getTvShowSearch(@Query("q") String searchShow, @Query("page") int pageNum);
}
