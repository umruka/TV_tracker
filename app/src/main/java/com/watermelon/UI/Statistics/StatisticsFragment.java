package com.watermelon.UI.Statistics;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.watermelon.Helpers.DateHelper;
import com.watermelon.R;

import java.util.List;

public class StatisticsFragment extends Fragment {

    private StatisticsViewModel statisticsViewModel;

    private TextView textViewShowsCount;
    private TextView textViewShowsWithNextEpisodes;
    private TextView textViewShowsNotEnded;
    private TextView textViewEpisodesCount;
    private TextView textViewWatchedEpisodesCount;
    private TextView textViewTotalRuntime;

    private ProgressBar progressBarShowsWithNextEpisodes;
    private ProgressBar progressBarShowsNotEnded;
    private ProgressBar progressBarEpisodeWatched;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        statisticsViewModel = new ViewModelProvider(this).get(StatisticsViewModel.class);
        statisticsViewModel.fetchStatisticsData();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.statistic_fragment, container, false);
        textViewShowsCount =  view.findViewById(R.id.statistics_show_count);
        textViewShowsWithNextEpisodes = view.findViewById(R.id.statistics_show_with_next_episodes_text);
        textViewShowsNotEnded = view.findViewById(R.id.statistics_show_count_still_running_progress_text);
        textViewEpisodesCount = view.findViewById(R.id.statistics_all_episodes_count);
        textViewWatchedEpisodesCount = view.findViewById(R.id.statistics_episodes_count_watched_progress_text);
        textViewTotalRuntime = view.findViewById(R.id.statistics_total_runtime);
        progressBarShowsWithNextEpisodes = view.findViewById(R.id.statistics_show_with_next_episodes);
        progressBarShowsNotEnded = view.findViewById(R.id.statistics_show_count_still_running_progress);
        progressBarEpisodeWatched = view.findViewById(R.id.statistics_episodes_count_watched_progress);


        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        statisticsViewModel.getStatisticsListObservable().observe(getViewLifecycleOwner(), new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> strings) {

                String showsCount = strings.get(0);
                String showsWithNextEpisodesCount = strings.get(1);
                String showsNotEndedCount = strings.get(2);
                String episodesCount =  strings.get(3);
                String episodeProgressCount = strings.get(4);
                String totalRuntime = strings.get(5);

                textViewShowsCount.setText(showsCount);
                textViewShowsWithNextEpisodes.setText(showsWithNextEpisodesCount + " with next episodes");
                progressBarShowsWithNextEpisodes.setMax(Integer.parseInt(showsCount));
                progressBarShowsWithNextEpisodes.setProgress(Integer.parseInt(showsWithNextEpisodesCount));
                textViewShowsNotEnded.setText(showsNotEndedCount + " running");
                progressBarShowsNotEnded.setMax(Integer.parseInt(showsCount));
                progressBarShowsNotEnded.setProgress(Integer.parseInt(showsNotEndedCount));
                textViewEpisodesCount.setText(episodesCount);
                textViewWatchedEpisodesCount.setText(episodeProgressCount + " episodes watched");
                progressBarEpisodeWatched.setProgress(Integer.parseInt(episodeProgressCount));
                progressBarEpisodeWatched.setMax(Integer.parseInt(episodesCount));
                textViewTotalRuntime.setText(DateHelper.getDaysString(totalRuntime));
            }
        });

    }

}
