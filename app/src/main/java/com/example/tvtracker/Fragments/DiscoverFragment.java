package com.example.tvtracker.Fragments;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tvtracker.R;
import com.example.tvtracker.TvShowModel.TvShow;
import com.example.tvtracker.TvShowModel.TvShowAdapter;
import com.example.tvtracker.TvShowModel.TvShowViewModel;
import com.example.tvtracker.Models.UpdateTvShowWatchingFlagParams;

import java.util.List;

public class DiscoverFragment extends Fragment {

    private Activity activity;
    private TvShowViewModel tvShowViewModel;

    public static DiscoverFragment newInstance() {
        return new DiscoverFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.discover_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.activity = getActivity();
        final RecyclerView recyclerView = getView().findViewById(R.id.dicover_recycler_view);
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

        adapter.setOnItemClickListener(new TvShowAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(TvShow tvShow) {

                int id =   tvShow.getTvShowId();
                 String flag = "yes";
                UpdateTvShowWatchingFlagParams params = new UpdateTvShowWatchingFlagParams(id, flag);
                tvShowViewModel.updateTvShowWatchingFlag(params);
                Toast.makeText(activity, "Done", Toast.LENGTH_SHORT).show();
            }
        });

        new Sync().execute();
        // TODO: Use the ViewModel
    }


    class Sync extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            tvShowViewModel.syncTvShows();
            return null;
        }
    }

}
