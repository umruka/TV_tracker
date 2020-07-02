package com.watermelon.Helpers;

import com.watermelon.Models.TvSeries;
import com.watermelon.Models.TvSeriesEpisode;
import com.watermelon.Models.TvSeriesFull;
import com.watermelon.Models.TvSeriesGenre;
import com.watermelon.Models.TvSeriesPicture;
import com.watermelon.Models.TvSeriesSeason;
import com.watermelon.Repository.Api.ApiModels.TvShowBasicInfo.JsonTvShow;
import com.watermelon.Repository.Api.ApiModels.TvShowBasicInfo.JsonTvShowBasicRoot;
import com.watermelon.Repository.Api.ApiModels.TvShowDetails.JsonEpisode;
import com.watermelon.Repository.Api.ApiModels.TvShowDetails.JsonTvShowFull;
import com.watermelon.Repository.Api.ApiModels.TvShowDetails.JsonTvShowFullRoot;

import java.util.ArrayList;
import java.util.List;

public class TvShowHelper {
    public static TvSeriesEpisode getNextWatched(List<TvSeriesEpisode> episodes) {
        for (int i = 0; i < episodes.size(); i++) {
            if (!episodes.get(i).isEpisodeWatched()) {
                return episodes.get(i);
            }
        }
        return null;
    }

    public static boolean getTvShowWatchlistState(TvSeries tvSeries){
        return tvSeries.isTvShowWatchingFlag();
    }

    public static boolean getTvShowState(List<TvSeriesEpisode> episodes) {
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

    public static List<TvSeriesSeason> getTvShowSeasons(List<TvSeriesEpisode> episodes) {
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
        //Last season
        tvSeriesSeasons.add(new TvSeriesSeason(currentSeason, currentSeasonEpisodes));
        return tvSeriesSeasons;
    }

    public static List<TvSeries> toTvShowArray(JsonTvShowBasicRoot root) {
        List<TvSeries> returnTvSeries = new ArrayList<>();
        List<JsonTvShow> TVShows = root.getTVShows();
        for(int i=0;i<TVShows.size();i++){

            JsonTvShow urlTvShow = TVShows.get(i);;

            int tvShowId = urlTvShow.getId();
            String tvShowName = urlTvShow.getName();
            String tvShowStatus = urlTvShow.getStatus();
            String tvShowStartDate = urlTvShow.getStartDate();
            String tvShowEndDate = urlTvShow.getEndDate();
            String tvShowCountry = urlTvShow.getCountry();
            String tvShowNetwork = urlTvShow.getNetwork();
            String tvShowImage = urlTvShow.getImageThumbnailPath();


            TvSeries tvSeries = new TvSeries(tvShowId, tvShowName, tvShowStartDate, tvShowEndDate, tvShowCountry, tvShowNetwork, tvShowStatus, tvShowImage);
            returnTvSeries.add(tvSeries);

        }
        return returnTvSeries;
    }

    public static TvSeriesFull jsonToModel(JsonTvShowFullRoot root){
        JsonTvShowFull jsonTvShowFull = root.getTvShow();
        TvSeries tvSeries = new TvSeries(jsonTvShowFull.getId(), jsonTvShowFull.getName(), jsonTvShowFull.getStartDate(), jsonTvShowFull.getEndDate(), jsonTvShowFull.getCountry(), jsonTvShowFull.getNetwork(), jsonTvShowFull.getStatus(), jsonTvShowFull.getImageThumbnailPath());
        tvSeries.setTvShowRuntime(String.valueOf(jsonTvShowFull.getRuntime()));
        tvSeries.setTvShowDesc(jsonTvShowFull.getDescription());
        tvSeries.setTvShowYoutubeLink(jsonTvShowFull.getYoutubeLink());
        tvSeries.setTvShowRating(jsonTvShowFull.getRating());

        int id = jsonTvShowFull.getId();
        List<JsonEpisode> episodes = jsonTvShowFull.getJsonEpisodes();
        List<String> genres = jsonTvShowFull.getGenres();
        List<String> pictures = jsonTvShowFull.getPictures();

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
