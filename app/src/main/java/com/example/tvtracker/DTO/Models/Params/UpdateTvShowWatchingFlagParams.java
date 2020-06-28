package com.example.tvtracker.DTO.Models.Params;

public class UpdateTvShowWatchingFlagParams {
    private int id;
    private boolean flag;

    public UpdateTvShowWatchingFlagParams(int id, boolean flag) {
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
