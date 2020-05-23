package com.example.tvtracker.Models.Params;


public class UpdateTvShowEpisodeWatchedFlagParams {
    private int id;
    private boolean flag;

    public UpdateTvShowEpisodeWatchedFlagParams(int id, boolean flag) {
        this.id = id;
        this.flag = flag;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean getFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }
}