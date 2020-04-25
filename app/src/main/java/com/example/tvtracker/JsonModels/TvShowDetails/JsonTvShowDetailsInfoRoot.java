package com.example.tvtracker.JsonModels.TvShowDetails;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class JsonTvShowDetailsInfoRoot {
    @SerializedName("tvShow")
    @Expose
    private JsonTvShowDetailsInfo jsonTvShowDetailsInfo;

    public JsonTvShowDetailsInfo getTvShow() {
        return jsonTvShowDetailsInfo;
    }

    public void setTvShow(JsonTvShowDetailsInfo jsonTvShowDetailsInfo) {
        this.jsonTvShowDetailsInfo = jsonTvShowDetailsInfo;
    }

}
