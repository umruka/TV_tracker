package com.example.tvtracker.Fragments;

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

import com.example.tvtracker.Adapters.EpisodeAdapter;
import com.example.tvtracker.MainActivity;
import com.example.tvtracker.Models.Params.UpdateTvShowEpisodeWatchedFlagParams;
import com.example.tvtracker.Models.TvShowEpisode;
import com.example.tvtracker.Models.TvShowSeason;
import com.example.tvtracker.R;
import com.example.tvtracker.ViewModels.EpisodesViewModel;
import com.google.gson.Gson;

public class EpisodesFragment extends Fragment implements EpisodeAdapter.OnItemClickListener {

    private Activity activity;
    private EpisodesViewModel episodesViewModel;

    private RecyclerView episodeList;

    public static EpisodesFragment newInstance() {
        return new EpisodesFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View  view = inflater.inflate(R.layout.fragment_episodes, container, false);
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
            String jsonEpisodes = getArguments().getString("data");
            Gson gson = new Gson();
            TvShowSeason showSeason  = gson.fromJson(jsonEpisodes, TvShowSeason.class);
            episodeAdapter.setEpisodes(showSeason.getEpisodes());
            episodeAdapter.setOnItemClickListener(this);

    }

    @Override
    public void onItemClick(TvShowEpisode episode) {
        int id = episode.getId();
        UpdateTvShowEpisodeWatchedFlagParams params = new UpdateTvShowEpisodeWatchedFlagParams(id, true);
        episodesViewModel.setWatchedFlag(params);
    }
}
