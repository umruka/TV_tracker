package com.example.tvtracker.Helpers;

import com.example.tvtracker.Models.TvShow;
import com.example.tvtracker.Models.TvShowEpisode;
import com.example.tvtracker.Models.TvShowSeason;

import java.util.ArrayList;
import java.util.List;

public class TvShowHelper {
    public static TvShowEpisode getNextWatched(List<TvShowEpisode> episodes) {
        for (int i = 0; i < episodes.size(); i++) {
            if (!episodes.get(i).isEpisodeWatched()) {
                return episodes.get(i);
            }
        }
        return null;
    }

    public static boolean getTvShowWatchlistState(TvShow tvShow){
        return tvShow.isTvShowWatchingFlag();
    }

    public static boolean getTvShowState(List<TvShowEpisode> episodes) {
        boolean state = true;
        for (int i = 0; i < episodes.size(); i++) {
            if (!episodes.get(i).isEpisodeWatched()) {
                state = false;
                return state;
            }
        }
        return state;
    }

    public static int getEpisodeProgress(List<TvShowEpisode> episodes) {
        int counter = 0;
        for (int i = 0; i < episodes.size(); i++) {
            if (episodes.get(i).isEpisodeWatched()) {
                counter++;
            }
        }
        return counter;
    }

    public static List<TvShowSeason> getTvShowSeasons(List<TvShowEpisode> episodes) {
        List<TvShowSeason> tvShowSeasons = new ArrayList<>();
        List<TvShowEpisode> currentSeasonEpisodes = new ArrayList<>();
        int currentSeason = 1;
        for (TvShowEpisode episode : episodes){
            int currentEpisodeSeason = episode.getEpisodeSeasonNum();
            if(currentEpisodeSeason == currentSeason){
                currentSeasonEpisodes.add(episode);
            }else{
                tvShowSeasons.add(new TvShowSeason(currentSeason, currentSeasonEpisodes));
                currentSeason++;
                currentSeasonEpisodes = new ArrayList<>();
                currentSeasonEpisodes.add(episode);
            }
        }
        //Last season
        tvShowSeasons.add(new TvShowSeason(currentSeason, currentSeasonEpisodes));
        return tvShowSeasons;
    }


}
