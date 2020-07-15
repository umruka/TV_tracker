package com.watermelon.UI.Discover;

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

import com.watermelon.UI.WatermelonActivity;
import com.watermelon.Models.TvSeries;
import com.watermelon.R;

import java.util.List;

public class DiscoverFragment extends Fragment implements DiscoverAdapter.OnItemClickListener {

    private Activity activity;
    private DiscoverViewModel discoverViewModel;
    private RecyclerView discoverRecyclerView;
    private TextView discoverFilterTitle;
    private NavController navController;
    private DiscoverAdapter discoverAdapter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.activity = getActivity();
        discoverViewModel = new ViewModelProvider(this).get(DiscoverViewModel.class);
        discoverAdapter = new DiscoverAdapter();
        discoverAdapter.setOnItemClickListener(this);
    }

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
        navController = Navigation.findNavController(getView());
        discoverRecyclerView.setAdapter(discoverAdapter);
        discoverViewModel.getDiscoverListObservable().observe(getViewLifecycleOwner(), new Observer<List<TvSeries>>() {
                    @Override
                    public void onChanged(List<TvSeries> tvSeries) {
                        discoverAdapter.setTvSeries(tvSeries);
                    }
                }
        );
    }

    @Override
    public void onItemClick(TvSeries tvSeries) {
        if (navController.getCurrentDestination().getId() == R.id.navigation_discover) {
            Bundle bundle = new Bundle();
            bundle.putString(WatermelonActivity.TVSERIES_ID, String.valueOf(tvSeries.getTvSeriesId()));
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
        if (item.isCheckable()) {
            item.setChecked(!item.isChecked());
        }
        switch (item.getItemId()) {
            case R.id.menu_fragment_search:
                if (navController.getCurrentDestination().getId() == R.id.navigation_discover) {
                    navController.navigate(R.id.action_navigation_discover_to_search_fragment);
                }
                break;
            case R.id.menu_filter_mostPopular:
                discoverAdapter.filter(WatermelonActivity.NETWORK_DEFAULT, WatermelonActivity.NETWORKS_CODE);
                discoverFilterTitle.setText("Most Popular");
                break;
            //Networks
            case R.id.netflix:
                discoverAdapter.filter(WatermelonActivity.NETWORK_NETFLIX, WatermelonActivity.NETWORKS_CODE);
                discoverFilterTitle.setText(WatermelonActivity.NETWORK_NETFLIX);
                break;
            case R.id.thecw:
                discoverAdapter.filter(WatermelonActivity.NETWORK_CW, WatermelonActivity.NETWORKS_CODE);
                discoverFilterTitle.setText(WatermelonActivity.NETWORK_CW);
                break;
            case R.id.hbo:
                discoverAdapter.filter(WatermelonActivity.NETWORK_HBO, WatermelonActivity.NETWORKS_CODE);
                discoverFilterTitle.setText(WatermelonActivity.NETWORK_HBO);
                break;
            case R.id.amc:
                discoverAdapter.filter(WatermelonActivity.NETWORK_AMC, WatermelonActivity.NETWORKS_CODE);
                discoverFilterTitle.setText(WatermelonActivity.NETWORK_AMC);
                break;
            case R.id.fox:
                discoverAdapter.filter(WatermelonActivity.NETWORK_FOX, WatermelonActivity.NETWORKS_CODE);
                discoverFilterTitle.setText(WatermelonActivity.NETWORK_FOX);
                break;

            //Statuses
            case R.id.running:
                discoverAdapter.filter(WatermelonActivity.STATUS_RUNNING, WatermelonActivity.STATUS_CODE);
                discoverFilterTitle.setText(WatermelonActivity.STATUS_RUNNING + " Series");
                break;
            case R.id.ended:
                discoverAdapter.filter(WatermelonActivity.STATUS_ENDED, WatermelonActivity.STATUS_CODE);
                discoverFilterTitle.setText(WatermelonActivity.STATUS_ENDED + " Series");
            default:
                break;
        }
        discoverRecyclerView.smoothScrollToPosition(0);
        return false;
    }


}
