package com.example.tvtracker.JsonModels.TvShowDetails;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class JsonTvShowDetailsRoot {
    @SerializedName("tvShow")
    @Expose
    private JsonTvShowDetails jsonTvShowDetails;

    public JsonTvShowDetails getTvShow() {
        return jsonTvShowDetails;
    }

    public void setTvShow(JsonTvShowDetails jsonTvShowDetails) {
        this.jsonTvShowDetails = jsonTvShowDetails;
    }

}
