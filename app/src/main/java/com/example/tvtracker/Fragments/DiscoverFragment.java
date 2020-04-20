package com.example.tvtracker.Fragments;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tvtracker.R;
import com.example.tvtracker.Models.TvShowDetails;
import com.example.tvtracker.ViewModels.TvShowDetailsViewModel;
import com.example.tvtracker.Models.TvShowBasic;
import com.example.tvtracker.Adapters.TvShowBasicAdapter;
import com.example.tvtracker.ViewModels.TvShowBasicViewModel;
import com.example.tvtracker.Models.UpdateTvShowBasicWatchingFlagParams;

import java.util.List;

public class DiscoverFragment extends Fragment {

    private Activity activity;
    private TvShowBasicViewModel tvShowBasicViewModel;
    private TvShowDetailsViewModel tvShowDetailsViewModel;

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

        final TvShowBasicAdapter adapter = new TvShowBasicAdapter();
        recyclerView.setAdapter(adapter);
        tvShowBasicViewModel = new ViewModelProvider(this).get(TvShowBasicViewModel.class);
        tvShowBasicViewModel.getAllTvShows().observe(getViewLifecycleOwner(), new Observer<List<TvShowBasic>>() {
            @Override
            public void onChanged(List<TvShowBasic> tvShowBasics) {
                adapter.setTvShowBasics(tvShowBasics);
            }
        });
        tvShowDetailsViewModel = new ViewModelProvider(this).get(TvShowDetailsViewModel.class);

        adapter.setOnItemClickListener(new TvShowBasicAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(TvShowBasic tvShowBasic) {

                int id = tvShowBasic.getTvShowId();
                String flag = "yes";
                UpdateTvShowBasicWatchingFlagParams params = new UpdateTvShowBasicWatchingFlagParams(id, flag);
                tvShowBasicViewModel.updateTvShowBasicWatchingFlag(params);
                tvShowDetailsViewModel.syncTvShowDetailsFromApi(id);

                Toast.makeText(activity, "Done", Toast.LENGTH_SHORT).show();
            }
        });
        tvShowBasicViewModel.syncTvShowBasicFromApi();
        // TODO: Use the ViewModel
    }




}
