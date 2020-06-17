package com.example.tvtracker.Fragments;

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

import com.example.tvtracker.Adapters.EpisodeAdapter;
import com.example.tvtracker.MainActivity;
import com.example.tvtracker.Models.Basic.Resource;
import com.example.tvtracker.Models.Basic.Status;
import com.example.tvtracker.Models.Params.UpdateTvShowEpisodeWatchedFlagParams;
import com.example.tvtracker.Models.TvShowEpisode;
import com.example.tvtracker.Models.TvShowSeason;
import com.example.tvtracker.R;
import com.example.tvtracker.ViewModels.EpisodesViewModel;

public class SeasonEpisodesFragment extends Fragment implements EpisodeAdapter.OnItemClickListener {

    private Activity activity;
    private EpisodesViewModel episodesViewModel;
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
        View  view = inflater.inflate(R.layout.fragment_episodes, container, false);
        progressBar = view.findViewById(R.id.season_progress);
        episodeList = view.findViewById(R.id.episode_recyclerView);
        episodeList.setLayoutManager(new LinearLayoutManager(activity));
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.activity = getActivity();

        EpisodeAdapter episodeAdapter = new EpisodeAdapter();


        episodesViewModel = new ViewModelProvider(this).get(EpisodesViewModel.class);
        // TODO: Use the ViewModel
            episodeList.setAdapter(episodeAdapter);
            mTvShowId   = Integer.parseInt(getArguments().getString(MainActivity.TVSHOW_ID));
            mSeasonNumber  = Integer.parseInt(getArguments().getString(MainActivity.TVSHOW_SEASON_NUM));
            episodesViewModel.getSeasonEpisodes(mTvShowId, mSeasonNumber);
            episodesViewModel.getSeasonObservable().observe(getViewLifecycleOwner(), new Observer<TvShowSeason>() {
                        @Override
                        public void onChanged(TvShowSeason tvShowSeasonResource) {
                                episodeAdapter.setEpisodes(tvShowSeasonResource.getEpisodes());
                                progressBar.setMax(episodesViewModel.getSeasonEpisodesCount());
                                progressBar.setProgress(episodesViewModel.getSeasonProgres());
                            }
                    });
                    episodeAdapter.setOnItemClickListener(this);
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

        episodesViewModel.setWatchedFlag(params);
        episodesViewModel.getSeasonEpisodes(mTvShowId, mSeasonNumber);
    }

}
