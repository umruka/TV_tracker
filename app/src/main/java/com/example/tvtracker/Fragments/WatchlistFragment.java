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
import com.example.tvtracker.Models.TvShow;
import com.example.tvtracker.R;
import com.example.tvtracker.Adapters.WatchlistAdapter;
import com.example.tvtracker.ViewModels.TvShowViewModel;

import java.util.List;

public class WatchlistFragment extends Fragment {


    private TvShowViewModel tvShowViewModel;

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

        tvShowViewModel = new ViewModelProvider(this).get(TvShowViewModel.class);
        tvShowViewModel.getAllWatchingTvShows().observe(getViewLifecycleOwner(), new Observer<List<TvShow>>() {
            @Override
            public void onChanged(List<TvShow> tvShows) {
                adapter.setTvShows(tvShows);
            }
        });

        adapter.setOnItemClickListener(new WatchlistAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(TvShow tvShow) {
//                Intent intent = new Intent(getContext(), TvShowFullActivity.class);
//                Bundle bundle = MainActivity.toBundle(tvShow);
//                intent.putExtra(MainActivity.TVSHOW_BUNDLE, bundle);
//                startActivity(intent);

                NavController navHostController = Navigation.findNavController(getView());
                if(navHostController.getCurrentDestination().getId() == R.id.navigation_watchlist){
                    Bundle bundle = new Bundle();
                    bundle.putString(MainActivity.TVSHOW_ID, String.valueOf(tvShow.getTvShowId()));
                    navHostController.navigate(R.id.action_navigation_watchlist_to_tvShowFullFragment, bundle);
                }
            }
        });

    }


}
