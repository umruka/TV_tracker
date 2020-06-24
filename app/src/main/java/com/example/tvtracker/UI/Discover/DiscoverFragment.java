package com.example.tvtracker.UI.Discover;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.tvtracker.MainActivity;
import com.example.tvtracker.DTO.Models.TvShow;
import com.example.tvtracker.R;
import com.example.tvtracker.DTO.Models.Params.UpdateTvShowWatchingFlagParams;

import java.util.List;

public class DiscoverFragment extends Fragment implements  DiscoverAdapter.OnItemClickListener,SwipeRefreshLayout.OnRefreshListener {

    private Activity activity;
    private DiscoverViewModel discoverViewModel;


    public static DiscoverFragment newInstance() {
        return new DiscoverFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_discover, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.activity = getActivity();
        final RecyclerView recyclerView = getView().findViewById(R.id.dicover_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(activity, 3));
        recyclerView.setHasFixedSize(true);
        RelativeLayout syncView = getView().findViewById(R.id.sync_view_discover);

        final DiscoverAdapter adapter = new DiscoverAdapter();
        recyclerView.setAdapter(adapter);
        discoverViewModel = new ViewModelProvider(this).get(DiscoverViewModel.class);
//        discoverViewModel.allInOne();
//        pullToRefresh.setRefreshing(true);
        /*
        discoverViewModel.getDiscoverListObservable().observe(getViewLifecycleOwner(), new Observer<Resource<List<TvShow>>>() {
            @Override
            public void onChanged(Resource<List<TvShow>> tvShows) {
//                        if(tvShows.status != Status.LOADING) {
//                            pullToRefresh.setRefreshing(false);
//                        }
//                        if(tvShows.data != null && !compareLists(adapter.getTvShowsShown(), tvShows.data)) {
                if(tvShows.status == Status.LOADING) {
                    recyclerView.setVisibility(View.GONE);
                    syncView.setVisibility(View.VISIBLE);
                }
                if(tvShows.status == Status.SUCCESS) {
                    syncView.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    adapter.setTvShows(tvShows.data);
                }
//                        }

//                        if(tvShows.status == Status.LOADING && tvShows.data == null) {
//                            refreshLayout.setRefreshing(true);
//                        }
//                        if(tvShows.status == Status.SUCCESS) {
//                            refreshLayout.setRefreshing(false);
//                        }
//                        if(tvShows.status == Status.ERROR) {
//                            Toast.makeText(activity, "ERROR SYNC", Toast.LENGTH_SHORT).show();
//                        }
                         */

//            }
//        });
        discoverViewModel.getDiscoverList().observe(getViewLifecycleOwner(), new Observer<List<TvShow>>() {
            @Override
            public void onChanged(List<TvShow> tvShows) {
                    adapter.setTvShows(tvShows);
                }
            }
        );
        adapter.setOnItemClickListener(this);
        androidx.appcompat.widget.AppCompatEditText editText = getView().findViewById(R.id.discover_search_bar);
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                NavController navHostController = Navigation.findNavController(view);
                if (navHostController.getCurrentDestination().getId() == R.id.navigation_discover) {
                    navHostController.navigate(R.id.action_navigation_discover_to_search_fragment);
                }
            }
        });

        // TODO: Use the ViewModel
    }

    public boolean compareLists(List<TvShow> baseList, List<TvShow> newList) {
        boolean areSame = true;
        if (baseList == null) {
            areSame = false;
        } else {
            for (TvShow item : newList) {
                if (!baseList.contains(item)) {
                    areSame = false;
                }
            }
        }
        return areSame;
    }

    @Override
    public void onResume() {
        super.onResume();
//        discoverViewModel.getDiscoverData();
    }


    @Override
    public void onButtonClick(TvShow tvShow) {

        int id = tvShow.getTvShowId();
        UpdateTvShowWatchingFlagParams params = new UpdateTvShowWatchingFlagParams(id, MainActivity.TVSHOW_WATCHING_FLAG_YES);
        discoverViewModel.updateTvShowBasicWatchingFlag(params);
        discoverViewModel.fetchDetailsForWatchlist(id);
        Toast.makeText(activity, "Done", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemClick(TvShow tvShow) {

        NavController navHostController = Navigation.findNavController(getView());
        if (navHostController.getCurrentDestination().getId() == R.id.navigation_discover) {
            Bundle bundle = new Bundle();
            bundle.putString(MainActivity.TVSHOW_ID, String.valueOf(tvShow.getTvShowId()));
            navHostController.navigate(R.id.action_navigation_discover_to_details_fragment, bundle);
        }
    }

    @Override
    public void onRefresh() {

    }
}