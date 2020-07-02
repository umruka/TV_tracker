package com.watermelon.UI.Watchlist;

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

import com.watermelon.Models.TvSeriesFull;
import com.watermelon.Helpers.TvSeriesHelper;
import com.watermelon.UI.WatermelonMainActivity;
import com.watermelon.R;

import java.util.List;

public class WatchlistFragment extends Fragment implements WatchlistAdapter.OnItemClickListener {


    private WatchlistViewModel watchlistViewModel;
    private WatchlistAdapter watchlistAdapter;
    private RecyclerView recyclerView;
    private RelativeLayout emptyLayout;

    private NavController navController;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        watchlistAdapter = new WatchlistAdapter();
        watchlistViewModel = new ViewModelProvider(this).get(WatchlistViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.watchlist_fragment, container, false);
        setHasOptionsMenu(true);
        recyclerView = view.findViewById(R.id.watchlist_recycler_view);
        recyclerView.setHasFixedSize(true);
        emptyLayout = view.findViewById(R.id.emptystatelayout);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        navController = Navigation.findNavController(getView());
        recyclerView.setAdapter(watchlistAdapter);
//        watchlistViewModel.fetchWatchlistData();
        watchlistViewModel.getWatchlistListObservable().observe(getViewLifecycleOwner(), new Observer<List<TvSeriesFull>>() {
                    @Override
                    public void onChanged(List<TvSeriesFull> tvSeriesFulls) {
                        watchlistAdapter.setTvSeries(tvSeriesFulls);
                        if(watchlistAdapter.getItemCount() == 0){
                            emptyLayout.setVisibility(View.VISIBLE);
                        }else if(watchlistAdapter.getItemCount() != 0){
                            emptyLayout.setVisibility(View.GONE);
                        }
                    }
                });

        watchlistAdapter.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(TvSeriesFull tvSeriesFull) {
        if (navController.getCurrentDestination().getId() == R.id.navigation_watchlist) {
            Bundle bundle = new Bundle();
            int id = tvSeriesFull.tvSeries.getTvSeriesId();
            bundle.putString(WatermelonMainActivity.TVSERIES_ID, String.valueOf(id));
            navController.navigate(R.id.action_navigation_watchlist_to_details_fragment, bundle);
        }
    }

    @Override
    public void onButtonClick(TvSeriesFull tvSeriesFull, int position) {

        if(TvSeriesHelper.getNextWatched(tvSeriesFull.episodes) != null) {
            int id = TvSeriesHelper.getNextWatched(tvSeriesFull.episodes).getId();
            Pair<Integer, Boolean> params = new Pair<>(id, WatermelonMainActivity.TVSERIES_WATCHED_EPISODE_FLAG_YES);
            watchlistViewModel.changeEpisodeWatchedFlag(params);
//            watchlistViewModel.fetchWatchlistData();
        }
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
