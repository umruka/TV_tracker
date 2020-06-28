package com.example.tvtracker.UI.Discover;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tvtracker.MainActivity;
import com.example.tvtracker.DTO.Models.TvShow;
import com.example.tvtracker.R;

import java.util.List;

public class DiscoverFragment extends Fragment implements DiscoverAdapter.OnItemClickListener {

    private Activity activity;
    private DiscoverViewModel discoverViewModel;
    private RecyclerView discoverRecyclerView;
    private NavController navController;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.discover_fragment, container, false);
        setHasOptionsMenu(true);

        discoverRecyclerView = view.findViewById(R.id.discover_recycler_view);
        discoverRecyclerView.setHasFixedSize(true);
        discoverRecyclerView.setLayoutManager(new GridLayoutManager(activity, 3));

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.activity = getActivity();
        navController = Navigation.findNavController(getView());
        final DiscoverAdapter adapter = new DiscoverAdapter();
        discoverRecyclerView.setAdapter(adapter);
        discoverViewModel = new ViewModelProvider(this).get(DiscoverViewModel.class);
        discoverViewModel.getDiscoverList().observe(getViewLifecycleOwner(), new Observer<List<TvShow>>() {
                    @Override
                    public void onChanged(List<TvShow> tvShows) {
                        adapter.setTvShows(tvShows);
                    }
                }
        );
        adapter.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(TvShow tvShow) {

        if (navController.getCurrentDestination().getId() == R.id.navigation_discover) {
            Bundle bundle = new Bundle();
            bundle.putString(MainActivity.TVSHOW_ID, String.valueOf(tvShow.getTvShowId()));
            navController.navigate(R.id.action_navigation_discover_to_details_fragment, bundle);
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.discover_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_fragment_search:
                if (navController.getCurrentDestination().getId() == R.id.navigation_discover) {
                    navController.navigate(R.id.action_navigation_discover_to_search_fragment);
                }
                break;
            case R.id.menu_filters:
                break;
            default:
                break;
        }
        return false;
    }
}
