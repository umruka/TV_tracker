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

import com.example.tvtracker.DTO.Models.StringHelper;
import com.example.tvtracker.MainActivity;
import com.example.tvtracker.DTO.Models.TvShowEpisode;
import com.example.tvtracker.DTO.Models.TvShowSeason;
import com.example.tvtracker.R;

public class SeasonEpisodesFragment extends Fragment implements SeasonEpisodesAdapter.OnItemClickListener {

    private Activity activity;
    private SeasonEpisodesViewModel seasonEpisodesViewModel;
    private int mTvShowId;
    private int mSeasonNumber;

    private TextView progressText;
    private ProgressBar progressBar;
    private RecyclerView episodeList;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View  view = inflater.inflate(R.layout.episodes_fragment, container, false);
        progressBar = view.findViewById(R.id.season_progress);
        episodeList = view.findViewById(R.id.episode_recyclerView);
        episodeList.setLayoutManager(new LinearLayoutManager(activity));
        progressText = view.findViewById(R.id.season_progress_text);
        setHasOptionsMenu(true);
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
                        public void onChanged(TvShowSeason tvShowSeason) {
                                seasonEpisodesAdapter.setEpisodes(tvShowSeason.getEpisodes());
                                 int seasonProgress = tvShowSeason.getSeasonProgress();
                                 int seasonEpisodesCount = tvShowSeason.getEpisodes().size();
                                progressBar.setMax(seasonEpisodesCount);
                                progressBar.setProgress(seasonProgress);


                                progressText.setText(StringHelper.addZero(seasonProgress) +  "/" + StringHelper.addZero(seasonEpisodesCount));

                            }
                    });
                    seasonEpisodesAdapter.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(TvShowEpisode episode) {
        int id = episode.getId();
        boolean isWatched = episode.isWatched();
        Pair<Integer, Boolean> params;
        if(!isWatched) {
            params = new Pair<>(id, MainActivity.TVSHOW_WATCHED_EPISODE_FLAG_YES);
        }else{
            params = new Pair<>(id, MainActivity.TVSHOW_WATCHED_EPISODE_FLAG_NO);
        }
        seasonEpisodesViewModel.setWatchedFlag(params);
        seasonEpisodesViewModel.getSeasonEpisodes(mTvShowId, mSeasonNumber);
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
