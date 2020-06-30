package com.example.tvtracker.UI.Watchlist;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Pair;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.example.tvtracker.MainActivity;
import com.example.tvtracker.DTO.Models.QueryModels.TvShowFull;
import com.example.tvtracker.R;
import com.example.tvtracker.UI.SeasonEpisodes.SeasonEpisodesViewModel;

import java.util.List;

public class WatchlistFragment extends Fragment {


    private WatchlistViewModel watchlistViewModel;
    private SeasonEpisodesViewModel seasonEpisodesViewModel;

    private RecyclerView recyclerView;
    private RelativeLayout emptyLayout;

    private NavController navController;

    public static WatchlistFragment newInstance() {
        return new WatchlistFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.watchlist_fragment, container, false);
        recyclerView = view.findViewById(R.id.watchlist_recycler_view);
        emptyLayout = view.findViewById(R.id.emptystatelayout);
        setHasOptionsMenu(true);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        navController = Navigation.findNavController(getView());
        recyclerView.setHasFixedSize(true);
        final WatchlistAdapter adapter = new WatchlistAdapter();
        recyclerView.setAdapter(adapter);
        seasonEpisodesViewModel = new ViewModelProvider(this).get(SeasonEpisodesViewModel.class);
        watchlistViewModel = new ViewModelProvider(this).get(WatchlistViewModel.class);

        watchlistViewModel.getWatchlistListObservable().observe(getViewLifecycleOwner(), new Observer<List<TvShowFull>>() {
                    @Override
                    public void onChanged(List<TvShowFull> tvShowFulls) {
                        adapter.setTvShows(tvShowFulls);
                        if(adapter.getItemCount() == 0){
                            emptyLayout.setVisibility(View.VISIBLE);
                        }else if(adapter.getItemCount() != 0){
                            emptyLayout.setVisibility(View.GONE);
                        }
                    }
                });
                watchlistViewModel.fetchWatchlistData();

                adapter.setOnItemClickListener(new WatchlistAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(TvShowFull tvShowFull) {
                        if (navController.getCurrentDestination().getId() == R.id.navigation_watchlist) {
                            Bundle bundle = new Bundle();
                            int id = tvShowFull.getTvShow().getTvShowId();
                            bundle.putString(MainActivity.TVSHOW_ID, String.valueOf(id));
                            navController.navigate(R.id.action_navigation_watchlist_to_details_fragment, bundle);
                        }
                    }

                    @Override
                    public void onButtonClick(TvShowFull tvShowFull) {

                        if(tvShowFull.getNextWatched() != null) {
                            int id = tvShowFull.getNextWatched().getId();
                            Pair<Integer, Boolean> params = new Pair<>(id, MainActivity.TVSHOW_WATCHED_EPISODE_FLAG_YES);
                            seasonEpisodesViewModel.setWatchedFlag(params);
                            watchlistViewModel.fetchWatchlistData();
                        }
                    }
                }
    );
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.watchlist_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_fragment_search:
                if (navController.getCurrentDestination().getId() == R.id.navigation_watchlist) {
                    navController.navigate(R.id.action_navigation_watchlist_to_fragment_search);
                }
                return true;
            default:
                return false;
        }
    }
}
