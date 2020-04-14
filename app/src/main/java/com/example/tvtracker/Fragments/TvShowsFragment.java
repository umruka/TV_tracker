package com.example.tvtracker.Fragments;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tvtracker.R;
import com.example.tvtracker.TvShowModel.TvShow;
import com.example.tvtracker.TvShowModel.TvShowAdapter;
import com.example.tvtracker.TvShowModel.TvShowViewModel;

import java.util.List;

public class TvShowsFragment extends Fragment {

    private TvShowViewModel tvShowViewModel;

    public static TvShowsFragment newInstance() {
        return new TvShowsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tv_series_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final RecyclerView recyclerView = getView().findViewById(R.id.tvshows_recycler_view);
        recyclerView.setHasFixedSize(true);

        final TvShowAdapter adapter = new TvShowAdapter();
        recyclerView.setAdapter(adapter);

        tvShowViewModel = ViewModelProviders.of(this).get(TvShowViewModel.class);
        tvShowViewModel.getAllTvShows().observe(getViewLifecycleOwner(), new Observer<List<TvShow>>() {
            @Override
            public void onChanged(List<TvShow> tvShows) {
            adapter.setTvShows(tvShows);
            }
        });
        new Sync().execute();
        // TODO: Use the ViewModel
    }


    class Sync extends AsyncTask<Void, Void, Void>{
        @Override
        protected Void doInBackground(Void... voids) {
            tvShowViewModel.syncTvShows();
            return null;
        }
    }
}
