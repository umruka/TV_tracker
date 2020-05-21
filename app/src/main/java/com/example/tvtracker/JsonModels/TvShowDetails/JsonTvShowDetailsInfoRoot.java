package com.example.tvtracker.JsonModels.TvShowDetails;

import com.example.tvtracker.Models.Params.UpdateTvShowDetailsParams;
import com.example.tvtracker.Models.QueryModels.TvShowTest;
import com.example.tvtracker.Models.TvShow;
import com.example.tvtracker.Models.TvShowEpisode;
import com.example.tvtracker.Models.TvShowGenre;
import com.example.tvtracker.Models.TvShowPicture;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class JsonTvShowDetailsInfoRoot {
    @SerializedName("tvShow")
    @Expose
    private JsonTvShowDetailsInfo jsonTvShowDetailsInfo;

    public JsonTvShowDetailsInfo getTvShow() {
        return jsonTvShowDetailsInfo;
    }

    public void setTvShow(JsonTvShowDetailsInfo jsonTvShowDetailsInfo) {
        this.jsonTvShowDetailsInfo = jsonTvShowDetailsInfo;
    }

    public TvShowTest toDetail(){
        TvShow tvShow = new TvShow(jsonTvShowDetailsInfo.getId(), jsonTvShowDetailsInfo.getName(), jsonTvShowDetailsInfo.getStartDate(), jsonTvShowDetailsInfo.getEndDate(), jsonTvShowDetailsInfo.getCountry(), jsonTvShowDetailsInfo.getNetwork(), jsonTvShowDetailsInfo.getStatus(), jsonTvShowDetailsInfo.getImagePath());
        tvShow.setTvShowDesc(jsonTvShowDetailsInfo.getDescription());
        tvShow.setTvShowYoutubeLink(jsonTvShowDetailsInfo.getYoutubeLink());
        tvShow.setTvShowRating(jsonTvShowDetailsInfo.getRating());

        int id = jsonTvShowDetailsInfo.getId();
        List<JsonTvShowDetailsEpisode> episodes = jsonTvShowDetailsInfo.getJsonTvShowDetailsEpisodes();
        List<String> genres = jsonTvShowDetailsInfo.getGenres();
        List<String> pictures = jsonTvShowDetailsInfo.getPictures();

        List<TvShowGenre> tvShowGenres = new ArrayList<>();
        for(String genre : genres) {
            tvShowGenres.add(new TvShowGenre(id,genre));
        }

        List<TvShowEpisode> tvShowEpisodes = new ArrayList<>();
        for(JsonTvShowDetailsEpisode episode : episodes) {
            tvShowEpisodes.add(new TvShowEpisode(id,episode.getSeason(),episode.getEpisode(),episode.getName(),episode.getAirDate()));
        }

        List<TvShowPicture> tvShowPictures = new ArrayList<>();
        for(String picture : pictures) {
            tvShowPictures.add(new TvShowPicture(id, picture));
        }

        return new TvShowTest(tvShow, tvShowEpisodes, tvShowGenres, tvShowPictures);


    }

}
