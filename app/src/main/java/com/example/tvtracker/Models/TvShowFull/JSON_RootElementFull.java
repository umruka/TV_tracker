package com.example.tvtracker.Models.TvShowFull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class JSON_RootElementFull {
    @SerializedName("tvShow")
    @Expose
    private TV_Show_Full tv_show_full;

    public TV_Show_Full getTvShow() {
        return tv_show_full;
    }

    public void setTvShow(TV_Show_Full tv_show_full) {
        this.tv_show_full = tv_show_full;
    }

}
