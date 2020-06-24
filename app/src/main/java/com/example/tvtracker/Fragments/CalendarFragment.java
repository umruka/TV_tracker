package com.example.tvtracker.Fragments;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tvtracker.Adapters.CalendarAdapter;
import com.example.tvtracker.Adapters.EpisodeAdapter;
import com.example.tvtracker.Adapters.WatchlistAdapter;
import com.example.tvtracker.MainActivity;
import com.example.tvtracker.Models.Basic.Resource;
import com.example.tvtracker.Models.Basic.Status;
import com.example.tvtracker.Models.CalendarTvShowEpisode;
import com.example.tvtracker.Models.QueryModels.TvShowFull;
import com.example.tvtracker.Models.QueryModels.fromDbCall;
import com.example.tvtracker.Models.TvShowEpisode;
import com.example.tvtracker.R;
import com.example.tvtracker.ViewModels.CalendarViewModel;
import com.example.tvtracker.ViewModels.DiscoverViewModel;
import com.example.tvtracker.ViewModels.WatchlistViewModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CalendarFragment extends Fragment implements CalendarAdapter.OnItemClickListener {
    private Activity activity;
    CalendarViewModel calendarViewModel;
    private RecyclerView calendarRecyclerView;

    public static CalendarFragment newInstance() {
        return new CalendarFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false); ;
        calendarRecyclerView = view.findViewById(R.id.calendar_recycler_view);
        calendarRecyclerView.setLayoutManager(new LinearLayoutManager(activity));
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.activity = getActivity();
        // TODO: Use the ViewModel
        calendarViewModel = new ViewModelProvider(this).get(CalendarViewModel.class);

        final CalendarAdapter adapter = new CalendarAdapter();
        calendarRecyclerView.setAdapter(adapter);
        calendarViewModel.getCalendarListObservable().observe(getViewLifecycleOwner(), new Observer<List<CalendarTvShowEpisode>>() {
            @Override
            public void onChanged(List<CalendarTvShowEpisode> tvShowFulls) {
                if(tvShowFulls != null){
                    adapter.setTvShows(tvShowFulls);
//                    List<String> asd = calendarViewModel.getUniqueDates();
//                    calendarRecyclerView.addItemDecoration(new DividerItemDecoration(activity,DividerItemDecoration.HORIZONTAL));
                }
            }
        });
        calendarViewModel.fetchData();
        adapter.setOnItemClickListener(this);

    }

    @Override
    public void onItemClick(CalendarTvShowEpisode calendarTvShowEpisode) {
        NavController navHostController = Navigation.findNavController(getView());
        if (navHostController.getCurrentDestination().getId() == R.id.navigation_calendar) {
            Bundle bundle = new Bundle();
            int id = calendarTvShowEpisode.getTvShowEpisode().getTvShowId();
            bundle.putString(MainActivity.TVSHOW_ID, String.valueOf(id));
            navHostController.navigate(R.id.action_navigation_calendar_to_fragment_details, bundle);
        }
    }
}
