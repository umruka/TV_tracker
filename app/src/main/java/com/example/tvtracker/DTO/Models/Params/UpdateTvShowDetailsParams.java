package com.example.tvtracker.DTO.Models.Params;

public class UpdateTvShowDetailsParams {
   private int id;
   private String desc;
   private String youtubeLink;
   private String rating;

    public UpdateTvShowDetailsParams(int id, String desc, String youtubeLink, String rating) {
        this.id = id;
        this.desc = desc;
        this.youtubeLink = youtubeLink;
        this.rating = rating;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getYoutubeLink() {
        return youtubeLink;
    }

    public void setYoutubeLink(String youtubeLink) {
        this.youtubeLink = youtubeLink;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }
}
