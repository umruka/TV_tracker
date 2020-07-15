package com.watermelon.UI.Calendar;

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

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.watermelon.Models.TvSeriesCalendarEpisode;
import com.watermelon.UI.WatermelonActivity;
import com.watermelon.R;

import java.util.List;

public class CalendarFragment extends Fragment implements CalendarAdapter.OnItemClickListener {
    private Activity activity;
    private CalendarViewModel calendarViewModel;
    private CalendarAdapter calendarAdapter;

    private RecyclerView calendarRecyclerView;
    private RelativeLayout calendarEmptyStateLayout;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.activity = getActivity();
        calendarViewModel = new ViewModelProvider(this).get(CalendarViewModel.class);
        calendarAdapter = new CalendarAdapter();
        calendarAdapter.setOnItemClickListener(this);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.calendar_fragment, container, false);

        calendarRecyclerView = view.findViewById(R.id.calendar_recycler_view);
        calendarRecyclerView.setLayoutManager(new LinearLayoutManager(activity));
        calendarEmptyStateLayout = view.findViewById(R.id.empty_state_calendar_layout);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        calendarRecyclerView.setAdapter(calendarAdapter);

        calendarViewModel.getCalendarListObservable().observe(getViewLifecycleOwner(), new Observer<List<TvSeriesCalendarEpisode>>() {
            @Override
            public void onChanged(List<TvSeriesCalendarEpisode> tvSeriesFulls) {
                if (tvSeriesFulls != null) {
                    calendarAdapter.setTvSeries(tvSeriesFulls);
                    if (calendarAdapter.getItemCount() == 0) {
                        calendarEmptyStateLayout.setVisibility(View.VISIBLE);
                    } else if (calendarAdapter.getItemCount() != 0) {
                        calendarEmptyStateLayout.setVisibility(View.GONE);
                    }
                }
            }
        });

    }

    @Override
    public void onItemClick(TvSeriesCalendarEpisode tvSeriesCalendarEpisode) {
        NavController navHostController = Navigation.findNavController(getView());
        if (navHostController.getCurrentDestination().getId() == R.id.navigation_calendar) {
            Bundle bundle = new Bundle();
            int id = tvSeriesCalendarEpisode.tvSeries.getTvSeriesId();
            bundle.putString(WatermelonActivity.TVSERIES_ID, String.valueOf(id));
            navHostController.navigate(R.id.action_navigation_calendar_to_fragment_details, bundle);
        }
    }
}
