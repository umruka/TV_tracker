package com.example.tvtracker.UI.Discover;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
    private TextView discoverFilterTitle;
    private NavController navController;
    private DiscoverAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.discover_fragment, container, false);
        setHasOptionsMenu(true);

        discoverRecyclerView = view.findViewById(R.id.discover_recycler_view);
        discoverRecyclerView.setHasFixedSize(true);
        discoverRecyclerView.setLayoutManager(new GridLayoutManager(activity, 3));

        discoverFilterTitle = view.findViewById(R.id.discover_text_view_filter_name);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.activity = getActivity();
        navController = Navigation.findNavController(getView());
        adapter = new DiscoverAdapter();
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
        MenuItem menuItemGenres = menu.findItem(R.id.menu_filters_networks);
        inflater.inflate(R.menu.discover_filters_sub_menu_networks, menuItemGenres.getSubMenu());

        MenuItem menuItemNetworks = menu.findItem(R.id.menu_filters_statuses);
        inflater.inflate(R.menu.discover_filters_sub_menu_statuses, menuItemNetworks.getSubMenu());

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        radioCheckChange(item);
        switch (item.getItemId()) {
            case R.id.menu_fragment_search:
                if (navController.getCurrentDestination().getId() == R.id.navigation_discover) {
                    navController.navigate(R.id.action_navigation_discover_to_search_fragment);
                }
                break;
             //Networks
            case R.id.mostPopular:
                adapter.filter(MainActivity.NETWORK_DEFAULT, MainActivity.NETWORKS_CODE);
                discoverFilterTitle.setText("Most Popular");
                break;
            case R.id.netflix:
                adapter.filter(MainActivity.NETWORK_NETFLIX, MainActivity.NETWORKS_CODE);
                discoverFilterTitle.setText(MainActivity.NETWORK_NETFLIX);
                break;
            case R.id.thecw:
                 adapter.filter(MainActivity.NETWORK_CW, MainActivity.NETWORKS_CODE);
                 discoverFilterTitle.setText(MainActivity.NETWORK_CW);
                break;
            case R.id.hbo:
                adapter.filter(MainActivity.NETWORK_HBO, MainActivity.NETWORKS_CODE);
                discoverFilterTitle.setText(MainActivity.NETWORK_HBO);
                break;
            case R.id.amc:
                adapter.filter(MainActivity.NETWORK_AMC, MainActivity.NETWORKS_CODE);
                discoverFilterTitle.setText(MainActivity.NETWORK_AMC);
                break;
            case R.id.fox:
                adapter.filter(MainActivity.NETWORK_FOX, MainActivity.NETWORKS_CODE);
                discoverFilterTitle.setText(MainActivity.NETWORK_FOX);
                break;

             //Statuses
            case R.id.running:
                adapter.filter(MainActivity.STATUS_RUNNING, MainActivity.STATUS_CODE);
                discoverFilterTitle.setText(MainActivity.STATUS_RUNNING + " Series");
                break;
            case R.id.ended:
                adapter.filter(MainActivity.STATUS_ENDED, MainActivity.STATUS_CODE);
                discoverFilterTitle.setText(MainActivity.STATUS_ENDED + " Series");
            default:
                break;
        }
        discoverRecyclerView.smoothScrollToPosition(0);
        return false;
    }

    private void radioCheckChange(MenuItem item){
        if (item.isCheckable()){
            item.setChecked(!item.isChecked());
        }
    }
}
