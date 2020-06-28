package com.example.tvtracker.UI.Search;

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
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.tvtracker.MainActivity;
import com.example.tvtracker.DTO.Models.TvShow;
import com.example.tvtracker.R;
import com.example.tvtracker.UI.Discover.DiscoverViewModel;

import java.util.List;

public class SearchFragment extends Fragment {

    private DiscoverViewModel discoverViewModel;

    private RecyclerView searchFragmentRecyclerView;
    private EditText searchFragmentEditText;

    public static SearchFragment newInstance() {
        return new SearchFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.search_fragment, container, false);
        searchFragmentRecyclerView = view.findViewById(R.id.search_recycler_view);
        searchFragmentRecyclerView.setHasFixedSize(true);
        searchFragmentEditText = view.findViewById(R.id.search_edit_text);

        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        discoverViewModel = new ViewModelProvider(this).get(DiscoverViewModel.class);

        final SearchAdapter adapter = new SearchAdapter();

        searchFragmentRecyclerView.setAdapter(adapter);
        discoverViewModel.getAllSearchWordTvShows().observe(getViewLifecycleOwner(), new Observer<List<TvShow>>() {
            @Override
            public void onChanged(List<TvShow> tvShows) {
                adapter.setTvShows(tvShows);
            }
        });

        adapter.setOnItemClickListener(new SearchAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(TvShow tvShow) {

                int id = tvShow.getTvShowId();
                discoverViewModel.fetchDetailsForWatchlist(id);
                NavController navHostController = Navigation.findNavController(getView());
                if(navHostController.getCurrentDestination().getId() == R.id.fragment_search){
                    Bundle bundle = new Bundle();
                    bundle.putString(MainActivity.TVSHOW_ID, String.valueOf(id));
                    navHostController.navigate(R.id.action_searchFragment_to_details_fragment, bundle);
                }

            }

//            @Override
//            public void onButtonClick(TvShow tvShow) {
//                discoverViewModel.insertOrUpdate(tvShow);
//                int id = tvShow.getTvShowId();
//                discoverViewModel.addTvShowToDb(tvShow);
//                UpdateTvShowWatchingFlagParams params = new UpdateTvShowWatchingFlagParams(id, TVSHOW_WATCHING_FLAG_YES);
//                discoverViewModel.updateTvShowBasicWatchingFlag(params);
//                discoverViewModel.fetchDetailsForWatchlist(id);
//                discoverViewModel.syncTvShowDetailsFromApi(id);
//
//                Toast.makeText(getContext(), "Done", Toast.LENGTH_SHORT).show();
//            }
        });


        searchFragmentEditText.addTextChangedListener(new TextWatcher() {
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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                NavController navHostController = Navigation.findNavController(getView());
                navHostController.popBackStack();
                return true;
            default:
                return false;
        }
    }








}
