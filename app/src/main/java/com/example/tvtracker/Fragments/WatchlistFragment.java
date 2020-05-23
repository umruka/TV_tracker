package com.example.tvtracker.Fragments;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tvtracker.MainActivity;
import com.example.tvtracker.Models.Basic.Resource;
import com.example.tvtracker.Models.QueryModels.TvShowTest;
import com.example.tvtracker.R;
import com.example.tvtracker.Adapters.WatchlistAdapter;
import com.example.tvtracker.ViewModels.WatchlistViewModel;

import java.util.List;

public class WatchlistFragment extends Fragment {


    private WatchlistViewModel watchlistViewModel;

    public static WatchlistFragment newInstance() {
        return new WatchlistFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_watchlist, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final RecyclerView recyclerView = getView().findViewById(R.id.watchlist_recycler_view);
        recyclerView.setHasFixedSize(true);

        final WatchlistAdapter adapter = new WatchlistAdapter();
        recyclerView.setAdapter(adapter);


        watchlistViewModel = new ViewModelProvider(this).get(WatchlistViewModel.class);
        watchlistViewModel.getWatchlistListObservable().observe(getViewLifecycleOwner(), new Observer<Resource<List<TvShowTest>>>() {
                    @Override
                    public void onChanged(Resource<List<TvShowTest>> tvShows) {
                        adapter.setTvShows(tvShows.data);
                    }
                });
        watchlistViewModel.fetchData();

                adapter.setOnItemClickListener(new WatchlistAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(TvShowTest tvShow) {

                        NavController navHostController = Navigation.findNavController(getView());
                        if (navHostController.getCurrentDestination().getId() == R.id.navigation_watchlist) {
                            Bundle bundle = new Bundle();
                            int id = tvShow.getTvShow().getTvShowId();
                            bundle.putString(MainActivity.TVSHOW_ID, String.valueOf(id));
                            navHostController.navigate(R.id.action_navigation_watchlist_to_details_fragment, bundle);
                        }
                    }
                });
    }


}
