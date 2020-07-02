package com.watermelon.Repository.Api;

import com.watermelon.Repository.Api.ApiModels.JsonTvSeriesSearchRoot;
import com.watermelon.Repository.Api.ApiModels.TvSeriesBasicInfo.JsonTvSeriesBasicRoot;
import com.watermelon.Repository.Api.ApiModels.TvSeriesDetails.JsonTvSeriesFullRoot;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {

    @GET("most-popular")
    Call<JsonTvSeriesBasicRoot> getTvSeriesBasic(@Query("page") int page);

    @GET("show-details")
    Call<JsonTvSeriesFullRoot> getTvSeriesDetailed(@Query("q") int tvSeriesId);

    @GET("search")
    Call<JsonTvSeriesSearchRoot> getTvSeriesSearch(@Query("q") String searchSeries, @Query("page") int pageNum);
}
