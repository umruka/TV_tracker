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

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.tvtracker.Adapters.TvShowBasicAdapter;
import com.example.tvtracker.MainActivity;
import com.example.tvtracker.Models.Params.UpdateTvShowWatchingFlagParams;
import com.example.tvtracker.Models.TvShow;
import com.example.tvtracker.R;
import com.example.tvtracker.ViewModels.DiscoverViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

import static com.example.tvtracker.MainActivity.TVSHOW_WATCHING_FLAG_YES;

public class SearchFragment extends Fragment {

    private DiscoverViewModel discoverViewModel;

    public static SearchFragment newInstance() {
        return new SearchFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        discoverViewModel = new ViewModelProvider(this).get(DiscoverViewModel.class);


        //needs thinking and fixing
//        BottomNavigationView navBar = getActivity().findViewById(R.id.bottom_navigation);
//        navBar.setVisibility(View.GONE);

        final RecyclerView recyclerView = getView().findViewById(R.id.search_recycler_view);
        recyclerView.setHasFixedSize(true);

        final TvShowBasicAdapter adapter = new TvShowBasicAdapter();
        recyclerView.setAdapter(adapter);
        discoverViewModel.getAllSearchWordTvShows().observe(getViewLifecycleOwner(), new Observer<List<TvShow>>() {
            @Override
            public void onChanged(List<TvShow> tvShows) {
                adapter.setTvShows(tvShows);
            }
        });



        adapter.setOnItemClickListener(new TvShowBasicAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(TvShow tvShow) {

//                discoverViewModel.showSearch(tvShow);
                int id = tvShow.getTvShowId();
                discoverViewModel.fetchDetailsForWatchlist(id);
                NavController navHostController = Navigation.findNavController(getView());
                if(navHostController.getCurrentDestination().getId() == R.id.fragment_search){
                    Bundle bundle = new Bundle();
                    bundle.putString(MainActivity.TVSHOW_ID, String.valueOf(id));
                    navHostController.navigate(R.id.action_searchFragment_to_details_fragment, bundle);
                }

            }

            @Override
            public void onButtonClick(TvShow tvShow) {
//                discoverViewModel.insertOrUpdate(tvShow);
                int id = tvShow.getTvShowId();
                UpdateTvShowWatchingFlagParams params = new UpdateTvShowWatchingFlagParams(id, TVSHOW_WATCHING_FLAG_YES);
                discoverViewModel.updateTvShowBasicWatchingFlag(params);
//                discoverViewModel.syncTvShowDetailsFromApi(id);

                Toast.makeText(getContext(), "Done", Toast.LENGTH_SHORT).show();
            }
        });

        EditText editText = getView().findViewById(R.id.search_edit_text);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
            if(editable.toString().equals("")){
                discoverViewModel.clearSearchedTvShows();
            }else{
                discoverViewModel.searchWord(editable.toString(),1);
            }
            }
        });




        // TODO: Use the ViewModel
    }





}
