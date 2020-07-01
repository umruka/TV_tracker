package com.example.tvtracker.UI.SeasonEpisodes;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Pair;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.tvtracker.Helpers.StringHelper;
import com.example.tvtracker.Helpers.TvShowHelper;
import com.example.tvtracker.UI.MainActivity;
import com.example.tvtracker.Models.TvShowEpisode;
import com.example.tvtracker.Models.TvShowSeason;
import com.example.tvtracker.R;

public class SeasonEpisodesFragment extends Fragment implements SeasonEpisodesAdapter.OnItemClickListener {

    private Activity activity;
    private SeasonEpisodesViewModel seasonEpisodesViewModel;
    private SeasonEpisodesAdapter seasonEpisodesAdapter;

    private int tvShowId;
    private int seasonNumber;

    private TextView progressText;
    private ProgressBar progressBar;
    private RecyclerView episodeList;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.activity = getActivity();
        seasonEpisodesViewModel = new ViewModelProvider(this).get(SeasonEpisodesViewModel.class);
        seasonEpisodesAdapter = new SeasonEpisodesAdapter();
        seasonEpisodesAdapter.setOnItemClickListener(this);

        tvShowId = Integer.parseInt(getArguments().getString(MainActivity.TVSHOW_ID));
        seasonNumber = Integer.parseInt(getArguments().getString(MainActivity.TVSHOW_SEASON_NUM));
        seasonEpisodesViewModel.getSeasonEpisodes(tvShowId, seasonNumber);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View  view = inflater.inflate(R.layout.episodes_fragment, container, false);
        setHasOptionsMenu(true);
        progressBar = view.findViewById(R.id.season_progress);
        episodeList = view.findViewById(R.id.episode_recyclerView);
        episodeList.setLayoutManager(new LinearLayoutManager(activity));
        progressText = view.findViewById(R.id.season_progress_text);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

            episodeList.setAdapter(seasonEpisodesAdapter);
            seasonEpisodesViewModel.getSeasonObservable().observe(getViewLifecycleOwner(), new Observer<TvShowSeason>() {
                        @Override
                        public void onChanged(TvShowSeason tvShowSeason) {
                                seasonEpisodesAdapter.setEpisodes(tvShowSeason.getEpisodes());
                                 int seasonProgress = TvShowHelper.getEpisodeProgress(tvShowSeason.getEpisodes());
                                 int seasonEpisodesCount = tvShowSeason.getEpisodes().size();
                                progressBar.setProgress(seasonProgress);
                                progressBar.setMax(seasonEpisodesCount);
                                progressText.setText(StringHelper.addZero(seasonProgress) +  "/" + StringHelper.addZero(seasonEpisodesCount));
                            }
                    });
    }

    @Override
    public void onItemClick(TvShowEpisode episode) {
        int id = episode.getId();
        boolean isWatched = episode.isEpisodeWatched();
        Pair<Integer, Boolean> params;
        if(!isWatched) {
            params = new Pair<>(id, MainActivity.TVSHOW_WATCHED_EPISODE_FLAG_YES);
        }else{
            params = new Pair<>(id, MainActivity.TVSHOW_WATCHED_EPISODE_FLAG_NO);
        }
        seasonEpisodesViewModel.changeEpisodeWatchedFlag(params);
        seasonEpisodesViewModel.getSeasonEpisodes(tvShowId, seasonNumber);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavController navHostController = Navigation.findNavController(getView());
                navHostController.popBackStack();
                break;
            default:
        }
        return false;
    }

}
