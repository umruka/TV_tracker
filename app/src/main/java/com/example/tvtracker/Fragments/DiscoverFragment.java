package com.example.tvtracker.Fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.tvtracker.MainActivity;
import com.example.tvtracker.Models.Basic.Resource;
import com.example.tvtracker.Models.Basic.Status;
import com.example.tvtracker.Models.TvShow;
import com.example.tvtracker.R;
import com.example.tvtracker.Adapters.TvShowBasicAdapter;
import com.example.tvtracker.ViewModels.TvShowViewModel;
import com.example.tvtracker.Models.Params.UpdateTvShowWatchingFlagParams;

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

        SwipeRefreshLayout refreshLayout = getView().findViewById(R.id.sync_bar);
        final TvShowBasicAdapter adapter = new TvShowBasicAdapter();
        recyclerView.setAdapter(adapter);
        tvShowViewModel = new ViewModelProvider(this).get(TvShowViewModel.class);
//        tvShowViewModel.allInOne();
        tvShowViewModel.getTvShowListObservable().observe(getViewLifecycleOwner(), new Observer<Resource<List<TvShow>>>() {
                    @Override
                    public void onChanged(Resource<List<TvShow>> tvShows) {

//                        if(tvShows.status == Status.LOADING && tvShows.data == null) {
//                            refreshLayout.setRefreshing(true);
//                        }
//                        if(tvShows.status == Status.SUCCESS) {
//                            refreshLayout.setRefreshing(false);
                            adapter.setTvShows(tvShows.data);
//                        }
//                        if(tvShows.status == Status.ERROR) {
//                            Toast.makeText(activity, "ERROR SYNC", Toast.LENGTH_SHORT).show();
//                        }
//                         */

                    }
                });
        tvShowViewModel.getData();

                adapter.setOnItemClickListener(new TvShowBasicAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(TvShow tvShow) {

                        NavController navHostController = Navigation.findNavController(getView());
                        if (navHostController.getCurrentDestination().getId() == R.id.navigation_discover) {
                            Bundle bundle = new Bundle();
                            bundle.putString(MainActivity.TVSHOW_ID, String.valueOf(tvShow.getTvShowId()));
                            navHostController.navigate(R.id.action_navigation_discover_to_tvShowFullFragment, bundle);
                        }
                    }

                    @Override
                    public void onButtonClick(TvShow tvShow) {
                        int id = tvShow.getTvShowId();
                        UpdateTvShowWatchingFlagParams params = new UpdateTvShowWatchingFlagParams(id, MainActivity.TVSHOW_WATCHING_FLAG_YES);
                        tvShowViewModel.updateTvShowBasicWatchingFlag(params);

                        Toast.makeText(activity, "Done", Toast.LENGTH_SHORT).show();
                    }
                });

        androidx.appcompat.widget.AppCompatEditText editText = getView().findViewById(R.id.discover_search_bar);
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                NavController navHostController = Navigation.findNavController(view);
                if(navHostController.getCurrentDestination().getId() == R.id.navigation_discover){
                    navHostController.navigate(R.id.action_navigation_discover_to_searchFragment);
                }
            }
        });

                // TODO: Use the ViewModel
    }




}
