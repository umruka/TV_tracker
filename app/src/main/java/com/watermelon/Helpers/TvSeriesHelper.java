package com.watermelon.Helpers;

import com.watermelon.Models.TvSeries;
import com.watermelon.Models.TvSeriesEpisode;
import com.watermelon.Models.TvSeriesFull;
import com.watermelon.Models.TvSeriesGenre;
import com.watermelon.Models.TvSeriesPicture;
import com.watermelon.Models.TvSeriesSeason;
import com.watermelon.Repository.Api.ApiModels.TvSeriesBasicInfo.JsonTvSeries;
import com.watermelon.Repository.Api.ApiModels.TvSeriesBasicInfo.JsonTvSeriesBasicRoot;
import com.watermelon.Repository.Api.ApiModels.TvSeriesDetails.JsonEpisode;
import com.watermelon.Repository.Api.ApiModels.TvSeriesDetails.JsonTvSeriesFull;
import com.watermelon.Repository.Api.ApiModels.TvSeriesDetails.JsonTvSeriesFullRoot;

import java.util.ArrayList;
import java.util.List;

public class TvSeriesHelper {
    public static TvSeriesEpisode getNextWatched(List<TvSeriesEpisode> episodes) {
        for (int i = 0; i < episodes.size(); i++) {
            if (!episodes.get(i).isEpisodeWatched()) {
                return episodes.get(i);
            }
        }
        return null;
    }

    public static boolean getTvSeriesWatchlistState(TvSeries tvSeries){
        return tvSeries.isTvSeriesWatchingFlag();
    }

    public static boolean getTvSeriesState(List<TvSeriesEpisode> episodes) {
        boolean state = true;
        for (int i = 0; i < episodes.size(); i++) {
            if (!episodes.get(i).isEpisodeWatched()) {
                state = false;
                return state;
            }
        }
        return state;
    }

    public static int getEpisodeProgress(List<TvSeriesEpisode> episodes) {
        int counter = 0;
        for (int i = 0; i < episodes.size(); i++) {
            if (episodes.get(i).isEpisodeWatched()) {
                counter++;
            }
        }
        return counter;
    }

    public static List<TvSeriesSeason> getTvSeriesSeasons(List<TvSeriesEpisode> episodes) {
        List<TvSeriesSeason> tvSeriesSeasons = new ArrayList<>();
        List<TvSeriesEpisode> currentSeasonEpisodes = new ArrayList<>();
        int currentSeason = 1;
        for (TvSeriesEpisode episode : episodes){
            int currentEpisodeSeason = episode.getEpisodeSeasonNum();
            if(currentEpisodeSeason == currentSeason){
                currentSeasonEpisodes.add(episode);
            }else{
                tvSeriesSeasons.add(new TvSeriesSeason(currentSeason, currentSeasonEpisodes));
                currentSeason++;
                currentSeasonEpisodes = new ArrayList<>();
                currentSeasonEpisodes.add(episode);
            }
        }
        tvSeriesSeasons.add(new TvSeriesSeason(currentSeason, currentSeasonEpisodes));
        return tvSeriesSeasons;
    }

    public static List<TvSeries> toTvSeriesArray(JsonTvSeriesBasicRoot root) {
        List<TvSeries> returnTvSeries = new ArrayList<>();
        List<JsonTvSeries> TVShows = root.getTVShows();
        for(int i=0;i<TVShows.size();i++){

            JsonTvSeries jsonTvSeries = TVShows.get(i);;

            int tvSeriesId = jsonTvSeries.getId();
            String tvSeriesName = jsonTvSeries.getName();
            String tvSeriesStartDate = jsonTvSeries.getStartDate();
            String tvSeriesEndDate = jsonTvSeries.getEndDate();
            String tvSeriesCountry = jsonTvSeries.getCountry();
            String tvSeriesNetwork = jsonTvSeries.getNetwork();
            String tvSeriesStatus = jsonTvSeries.getStatus();
            String tvSeriesImage = jsonTvSeries.getImageThumbnailPath();


            TvSeries tvSeries = new TvSeries(tvSeriesId, tvSeriesName, tvSeriesStartDate, tvSeriesEndDate, tvSeriesCountry, tvSeriesNetwork, tvSeriesStatus, tvSeriesImage);
            returnTvSeries.add(tvSeries);

        }
        return returnTvSeries;
    }

    public static TvSeriesFull jsonToModel(JsonTvSeriesFullRoot root){
        JsonTvSeriesFull jsonTvSeriesFull = root.getTvShow();
        TvSeries tvSeries = new TvSeries(jsonTvSeriesFull.getId(), jsonTvSeriesFull.getName(), jsonTvSeriesFull.getStartDate(), jsonTvSeriesFull.getEndDate(), jsonTvSeriesFull.getCountry(), jsonTvSeriesFull.getNetwork(), jsonTvSeriesFull.getStatus(), jsonTvSeriesFull.getImageThumbnailPath());
        tvSeries.setTvSeriesRuntime(String.valueOf(jsonTvSeriesFull.getRuntime()));
        tvSeries.setTvSeriesDesc(jsonTvSeriesFull.getDescription());
        tvSeries.setTvSeriesYoutubeLink(jsonTvSeriesFull.getYoutubeLink());
        tvSeries.setTvSeriesRating(jsonTvSeriesFull.getRating());

        int id = jsonTvSeriesFull.getId();
        List<JsonEpisode> episodes = jsonTvSeriesFull.getJsonEpisodes();
        List<String> genres = jsonTvSeriesFull.getGenres();
        List<String> pictures = jsonTvSeriesFull.getPictures();

        List<TvSeriesGenre> tvSeriesGenres = new ArrayList<>();
        for(String genre : genres) {
            tvSeriesGenres.add(new TvSeriesGenre(id,genre));
        }

        List<TvSeriesEpisode> tvSeriesEpisodes = new ArrayList<>();
        for(JsonEpisode episode : episodes) {
            tvSeriesEpisodes.add(new TvSeriesEpisode(id,episode.getSeason(),episode.getEpisode(),episode.getName(),episode.getAirDate()));
        }

        List<TvSeriesPicture> tvSeriesPictures = new ArrayList<>();
        for(String picture : pictures) {
            tvSeriesPictures.add(new TvSeriesPicture(id, picture));
        }
        return new TvSeriesFull(tvSeries, tvSeriesGenres, tvSeriesEpisodes, tvSeriesPictures);
    }


}
