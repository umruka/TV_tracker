package com.example.tvtracker.Fragments;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tvtracker.R;
import com.example.tvtracker.WatchlistModel.Watchlist;
import com.example.tvtracker.WatchlistModel.WatchlistAdapter;
import com.example.tvtracker.WatchlistModel.WatchlistViewModel;

import java.util.List;

public class WatchlistFragment extends Fragment {

    private WatchlistViewModel watchlistViewModel;

    public static WatchlistFragment newInstance() {
        return new WatchlistFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.watchlist_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final RecyclerView recyclerView = getView().findViewById(R.id.watchlist_recycler_view);
        recyclerView.setHasFixedSize(true);

        final WatchlistAdapter adapter = new WatchlistAdapter();
        recyclerView.setAdapter(adapter);

        watchlistViewModel = ViewModelProviders.of(this).get(WatchlistViewModel.class);
        watchlistViewModel.getAllWatchlist().observe(getViewLifecycleOwner(), new Observer<List<Watchlist>>() {
            @Override
            public void onChanged(List<Watchlist> watchlists) {
            adapter.setWatchlists(watchlists);
            }
        });

    }


}
