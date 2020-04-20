package com.example.tvtracker.Models;

public class UpdateTvShowBasicWatchingFlagParams {
    int id;
    String flag;

    public UpdateTvShowBasicWatchingFlagParams(int id, String flag) {
        this.id = id;
        this.flag = flag;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }
}
