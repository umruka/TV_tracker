package com.example.tvtracker.Fragments;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

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
import com.google.gson.Gson;

public class EpisodesFragment extends Fragment implements EpisodeAdapter.OnItemClickListener {

    private Activity activity;
    private EpisodesViewModel episodesViewModel;
    private int mTvShowId;
    private int mSeasonNumber;


    private ProgressBar progressBar;
    private RecyclerView episodeList;

    public static EpisodesFragment newInstance() {
        return new EpisodesFragment();
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
            episodesViewModel.getSeasonObservable().observe(getViewLifecycleOwner(), new Observer<Resource<TvShowSeason>>() {
                        @Override
                        public void onChanged(Resource<TvShowSeason> tvShowSeasonResource) {
                            if(tvShowSeasonResource.data != null && tvShowSeasonResource.status != Status.LOADING && tvShowSeasonResource.data.getEpisodes() != null) {
                                episodeAdapter.setEpisodes(tvShowSeasonResource.data.getEpisodes());
//                                episodesViewModel.getSeasonEpisodes(id, seasonNum);
                                progressBar.setMax(episodesViewModel.getSeasonEpisodesCount());
                                progressBar.setProgress(episodesViewModel.getSeasonProgres());
                            }
                        }
                    });
                    episodeAdapter.setOnItemClickListener(this);
            episodesViewModel.getSeasonEpisodes(mTvShowId, mSeasonNumber);
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
        refreshData(mTvShowId, mSeasonNumber);
    }

    private void refreshData(int id, int seasonNum) {
        episodesViewModel.getSeasonEpisodes(id, seasonNum);
    }

}
