package com.example.tvtracker.JsonModels.TvShowDetails;

import com.example.tvtracker.Models.QueryModels.TvShowFull;
import com.example.tvtracker.Models.TvShow;
import com.example.tvtracker.Models.TvShowEpisode;
import com.example.tvtracker.Models.TvShowGenre;
import com.example.tvtracker.Models.TvShowPicture;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

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
