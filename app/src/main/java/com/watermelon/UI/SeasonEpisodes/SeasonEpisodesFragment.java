package com.watermelon.UI.SeasonEpisodes;

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

import com.watermelon.Helpers.StringHelper;
import com.watermelon.Helpers.TvSeriesHelper;
import com.watermelon.Models.TvSeriesEpisode;
import com.watermelon.UI.WatermelonActivity;
import com.watermelon.Models.TvSeriesSeason;
import com.watermelon.R;

public class SeasonEpisodesFragment extends Fragment implements SeasonEpisodesAdapter.OnItemClickListener {

    private Activity activity;
    private SeasonEpisodesViewModel seasonEpisodesViewModel;
    private SeasonEpisodesAdapter seasonEpisodesAdapter;

    private int tvSeriesId;
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

        tvSeriesId = Integer.parseInt(getArguments().getString(WatermelonActivity.TVSERIES_ID));
        seasonNumber = Integer.parseInt(getArguments().getString(WatermelonActivity.TVSERIES_SEASON_NUM));
        seasonEpisodesViewModel.getSeasonEpisodes(tvSeriesId, seasonNumber);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.episodes_fragment, container, false);
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
        seasonEpisodesViewModel.getSeasonEpisodesObservable().observe(getViewLifecycleOwner(), new Observer<TvSeriesSeason>() {
            @Override
            public void onChanged(TvSeriesSeason tvSeriesSeason) {
                seasonEpisodesAdapter.setEpisodes(tvSeriesSeason.getEpisodes());
                int seasonProgress = TvSeriesHelper.getEpisodeProgress(tvSeriesSeason.getEpisodes());
                int seasonEpisodesCount = tvSeriesSeason.getEpisodes().size();
                progressBar.setMax(seasonEpisodesCount);
                progressBar.setProgress(seasonProgress);
                progressText.setText(StringHelper.addZero(seasonProgress) + "/" + StringHelper.addZero(seasonEpisodesCount));
            }
        });
    }

    @Override
    public void onItemClick(TvSeriesEpisode episode) {
        int id = episode.getId();
        boolean isWatched = episode.isEpisodeWatched();
        Pair<Integer, Boolean> params;
        if (!isWatched) {
            params = new Pair<>(id, WatermelonActivity.TVSERIES_WATCHED_EPISODE_FLAG_YES);
        } else {
            params = new Pair<>(id, WatermelonActivity.TVSERIES_WATCHED_EPISODE_FLAG_NO);
        }
        seasonEpisodesViewModel.changeEpisodeWatchedFlag(params);
        seasonEpisodesViewModel.getSeasonEpisodes(tvSeriesId, seasonNumber);
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
