package com.example.tvtracker.JsonModels.TvShowBasicInfo;

import com.example.tvtracker.Models.TvShow;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class JsonTvShowBasicInfoRoot {

    @SerializedName("total")
    @Expose
    private String total;
    @SerializedName("page")
    @Expose
    private Integer page;
    @SerializedName("pages")
    @Expose
    private Integer pages;
    @SerializedName("tv_shows")
    @Expose
    private List<JsonTvShowBasicInfo> TVShows = null;

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getPages() {
        return pages;
    }

    public void setPages(Integer pages) {
        this.pages = pages;
    }

    public List<JsonTvShowBasicInfo> getTVShows() {
        return TVShows;
    }

    public void setTVShows(List<JsonTvShowBasicInfo> TVShows) {
        this.TVShows = TVShows;
    }

    public List<TvShow> toTvShowArray() {
        List<TvShow> returnTvShows = new ArrayList<>();
        for(int i=0;i<TVShows.size();i++){

            JsonTvShowBasicInfo urlTvShow = TVShows.get(i);;

            int tvShowId = urlTvShow.getId();
            String tvShowName = urlTvShow.getName();
            String tvShowStatus = urlTvShow.getStatus();
            String tvShowStartDate = urlTvShow.getStartDate();
            String tvShowEndDate = urlTvShow.getEndDate();
            String tvShowCountry = urlTvShow.getCountry();
            String tvShowNetwork = urlTvShow.getNetwork();
            String tvShowImage = urlTvShow.getImageThumbnailPath();


            TvShow tvShow = new TvShow(tvShowId, tvShowName, tvShowStartDate, tvShowEndDate, tvShowCountry, tvShowNetwork, tvShowStatus, tvShowImage);
            returnTvShows.add(tvShow);

        }
        return  returnTvShows;
    }


}
