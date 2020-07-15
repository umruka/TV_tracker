package com.watermelon.Repository.Api.ApiModels;

import com.watermelon.Repository.Api.ApiModels.TvSeriesBasicInfo.JsonTvSeries;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class JsonTvSeriesSearchRoot {

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
        private List<JsonTvSeries> tvShows = null;

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

        public List<JsonTvSeries> getTvShows() {
            return tvShows;
        }

        public void setTvShows(List<JsonTvSeries> tvShows) {
            this.tvShows = tvShows;
        }

    }
