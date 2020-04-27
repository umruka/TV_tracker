package com.example.tvtracker.Fragments;

import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tvtracker.Adapters.CalendarAdapter;
import com.example.tvtracker.R;
import com.example.tvtracker.ViewModels.TvShowViewModel;

public class CalendarFragment extends Fragment {
    private Activity activity;
    TvShowViewModel tvShowViewModel;

    public static CalendarFragment newInstance() {
        return new CalendarFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.calendar_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.activity = getActivity();
        // TODO: Use the ViewModel
        tvShowViewModel = new ViewModelProvider(this).get(TvShowViewModel.class);

        final RecyclerView recyclerView = getView().findViewById(R.id.calendar_recycler_view);
        recyclerView.setHasFixedSize(true);

        final CalendarAdapter adapter = new CalendarAdapter();
        recyclerView.setAdapter(adapter);
        adapter.setTvShows(tvShowViewModel.tvShowPicturesById(23455));

    }

}
