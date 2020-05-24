package com.example.tvtracker.JsonModels.TvShowDetails;

import com.example.tvtracker.Models.QueryModels.TvShowTest;
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

    public TvShowTest toDetail(){
        TvShow tvShow = new TvShow(jsonTvShowFull.getId(), jsonTvShowFull.getName(), jsonTvShowFull.getStartDate(), jsonTvShowFull.getEndDate(), jsonTvShowFull.getCountry(), jsonTvShowFull.getNetwork(), jsonTvShowFull.getStatus(), jsonTvShowFull.getImagePath());
        tvShow.setTvShowDesc(jsonTvShowFull.getDescription());
        tvShow.setTvShowYoutubeLink(jsonTvShowFull.getYoutubeLink());
        tvShow.setTvShowRating(jsonTvShowFull.getRating());

        int id = jsonTvShowFull.getId();
        List<JsonEpisode> episodes = jsonTvShowFull.getJsonEpisodes();
        List<String> genres = jsonTvShowFull.getGenres();
        List<String> pictures = jsonTvShowFull.getPictures();

        List<TvShowGenre> tvShowGenres = new ArrayList<>();
        for(String genre : genres) {
            tvShowGenres.add(new TvShowGenre(id,genre));
        }

        List<TvShowEpisode> tvShowEpisodes = new ArrayList<>();
        for(JsonEpisode episode : episodes) {
            tvShowEpisodes.add(new TvShowEpisode(id,episode.getSeason(),episode.getEpisode(),episode.getName(),episode.getAirDate()));
        }

        List<TvShowPicture> tvShowPictures = new ArrayList<>();
        for(String picture : pictures) {
            tvShowPictures.add(new TvShowPicture(id, picture));
        }

        return new TvShowTest(tvShow, tvShowEpisodes, tvShowGenres, tvShowPictures);


    }

}
