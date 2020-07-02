package com.watermelon.Repository.Api.ApiModels.TvSeriesDetails;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class JsonTvSeriesFullRoot {
    @SerializedName("tvShow")
    @Expose
    private JsonTvSeriesFull jsonTvSeriesFull;

    public JsonTvSeriesFull getTvShow() {
        return jsonTvSeriesFull;
    }

    public void setTvShow(JsonTvSeriesFull jsonTvSeriesFull) {
        this.jsonTvSeriesFull = jsonTvSeriesFull;
    }



}
