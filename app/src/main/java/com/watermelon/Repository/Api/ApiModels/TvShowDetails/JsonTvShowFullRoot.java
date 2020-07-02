package com.watermelon.Repository.Api.ApiModels.TvShowDetails;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class JsonTvShowFullRoot {
    @SerializedName("tvShow")
    @Expose
    private JsonTvShowFull jsonTvShowFull;

    public JsonTvShowFull getTvShow() {
        return jsonTvShowFull;
    }

    public void setTvShow(JsonTvShowFull jsonTvShowFull) {
        this.jsonTvShowFull = jsonTvShowFull;
    }



}
