package com.example.tvtracker.Models.Params;

public class SeasonTvShowParams {
    private int id;
    private int seasonNum;

    public SeasonTvShowParams(int id, int seasonNum) {
        this.id = id;
        this.seasonNum = seasonNum;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSeasonNum() {
        return seasonNum;
    }

    public void setSeasonNum(int seasonNum) {
        this.seasonNum = seasonNum;
    }
}
