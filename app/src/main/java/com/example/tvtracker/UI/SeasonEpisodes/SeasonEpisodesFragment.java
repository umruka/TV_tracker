package com.example.tvtracker.UI.SeasonEpisodes;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.tvtracker.MainActivity;
import com.example.tvtracker.DTO.Models.Params.UpdateTvShowEpisodeWatchedFlagParams;
import com.example.tvtracker.DTO.Models.TvShowEpisode;
import com.example.tvtracker.DTO.Models.TvShowSeason;
import com.example.tvtracker.R;

public class SeasonEpisodesFragment extends Fragment implements SeasonEpisodesAdapter.OnItemClickListener {

    private Activity activity;
    private SeasonEpisodesViewModel seasonEpisodesViewModel;
    private int mTvShowId;
    private int mSeasonNumber;


    private ProgressBar progressBar;
    private RecyclerView episodeList;

    public static SeasonEpisodesFragment newInstance() {
        return new SeasonEpisodesFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View  view = inflater.inflate(R.layout.episodes_fragment, container, false);
        progressBar = view.findViewById(R.id.season_progress);
        episodeList = view.findViewById(R.id.episode_recyclerView);
        episodeList.setLayoutManager(new LinearLayoutManager(activity));
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.activity = getActivity();

        SeasonEpisodesAdapter seasonEpisodesAdapter = new SeasonEpisodesAdapter();


        seasonEpisodesViewModel = new ViewModelProvider(this).get(SeasonEpisodesViewModel.class);
        // TODO: Use the ViewModel
            episodeList.setAdapter(seasonEpisodesAdapter);
            mTvShowId   = Integer.parseInt(getArguments().getString(MainActivity.TVSHOW_ID));
            mSeasonNumber  = Integer.parseInt(getArguments().getString(MainActivity.TVSHOW_SEASON_NUM));
            seasonEpisodesViewModel.getSeasonEpisodes(mTvShowId, mSeasonNumber);
            seasonEpisodesViewModel.getSeasonObservable().observe(getViewLifecycleOwner(), new Observer<TvShowSeason>() {
                        @Override
                        public void onChanged(TvShowSeason tvShowSeasonResource) {
                                seasonEpisodesAdapter.setEpisodes(tvShowSeasonResource.getEpisodes());
                                progressBar.setMax(seasonEpisodesViewModel.getSeasonEpisodesCount());
                                progressBar.setProgress(seasonEpisodesViewModel.getSeasonProgres());
                            }
                    });
                    seasonEpisodesAdapter.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(int position, TvShowEpisode episode) {
        int id = episode.getId();
        boolean isWatched = episode.isWatched();
        UpdateTvShowEpisodeWatchedFlagParams params;
        if(!isWatched) {
            params = new UpdateTvShowEpisodeWatchedFlagParams(id, MainActivity.TVSHOW_WATCHED_EPISODE_FLAG_YES);
        }else{
            params = new UpdateTvShowEpisodeWatchedFlagParams(id, MainActivity.TVSHOW_WATCHED_EPISODE_FLAG_NO);
        }
        seasonEpisodesViewModel.setWatchedFlag(params);
        seasonEpisodesViewModel.getSeasonEpisodes(mTvShowId, mSeasonNumber);
    }

}
