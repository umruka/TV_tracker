package com.example.tvtracker.Fragments;

import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tvtracker.Adapters.CalendarAdapter;
import com.example.tvtracker.Models.QueryModels.fromDbCall;
import com.example.tvtracker.Models.TvShowEpisode;
import com.example.tvtracker.R;
import com.example.tvtracker.ViewModels.DiscoverViewModel;

import java.util.List;

public class CalendarFragment extends Fragment {
    private Activity activity;
    DiscoverViewModel discoverViewModel;

    public static CalendarFragment newInstance() {
        return new CalendarFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_calendar, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.activity = getActivity();
        // TODO: Use the ViewModel
        discoverViewModel = new ViewModelProvider(this).get(DiscoverViewModel.class);

        final RecyclerView recyclerView = getView().findViewById(R.id.calendar_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(activity, 3));

        final CalendarAdapter adapter = new CalendarAdapter();
        recyclerView.setAdapter(adapter);
//        adapter.setTvShows(discoverViewModel.tvShowPicturesById(23455));
//        List<TvShowEpisode> episodes = discoverViewModel.tvShowEpisodesById(23455);
//        List<fromDbCall> p = discoverViewModel.getTvShowWithPicturesById(23455);

    }

}
