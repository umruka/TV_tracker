package com.watermelon.UI.Search;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
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
import android.widget.ImageView;

import com.watermelon.Helpers.KeyboardHelper;
import com.watermelon.Models.TvSeries;
import com.watermelon.UI.WatermelonMainActivity;
import com.watermelon.R;

import java.util.List;

public class SearchFragment extends Fragment implements SearchAdapter.OnItemClickListener, TextWatcher {

    private SearchViewModel searchViewModel;
    private Activity activity;
    private SearchAdapter adapterSearch;
    private RecyclerView searchFragmentRecyclerView;
    private ImageView searchFragmentImageViewIcon;
    private EditText searchFragmentEditText;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
        searchViewModel = new ViewModelProvider(this).get(SearchViewModel.class);
        adapterSearch = new SearchAdapter();
        adapterSearch.setOnItemClickListener(this);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.search_fragment, container, false);
        setHasOptionsMenu(true);
        searchFragmentRecyclerView = view.findViewById(R.id.search_recycler_view);
        searchFragmentRecyclerView.setHasFixedSize(true);
        searchFragmentEditText = view.findViewById(R.id.search_edit_text);
        searchFragmentImageViewIcon = view.findViewById(R.id.search_image_view_icon);
        searchFragmentImageViewIcon.setImageResource(R.drawable.ic_search);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        searchFragmentRecyclerView.setAdapter(adapterSearch);
        searchViewModel.getDiscoverList().observe(getViewLifecycleOwner(), new Observer<List<TvSeries>>() {
            @Override
            public void onChanged(List<TvSeries> tvSeries) {
             adapterSearch.setTvSeries(tvSeries);
            }
        });
        searchViewModel.getAllSearchWordTvSeries().observe(getViewLifecycleOwner(), new Observer<List<TvSeries>>() {
            @Override
            public void onChanged(List<TvSeries> tvSeries) {
                adapterSearch.setTvSeries(tvSeries);
            }
        });

        searchFragmentEditText.addTextChangedListener(this);
    }


    @Override
    public void onItemClick(TvSeries tvSeries) {
        int id = tvSeries.getTvSeriesId();
        searchViewModel.fetchTvSeriesDetails(id);
        NavController navHostController = Navigation.findNavController(getView());
        if(navHostController.getCurrentDestination().getId() == R.id.fragment_search){
            Bundle bundle = new Bundle();
            bundle.putString(WatermelonMainActivity.TVSERIES_ID, String.valueOf(id));
            navHostController.navigate(R.id.action_searchFragment_to_details_fragment, bundle);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        searchFragmentRecyclerView.setAdapter(adapterSearch);

    }

    @Override
    public void afterTextChanged(Editable editable) {
        if(editable.toString().equals("")){
            searchViewModel.clearSearchedTvSeries();
            adapterSearch.notifyDataSetChanged();
        }else{
            searchViewModel.searchTvSeriesData(editable.toString());
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                NavController navHostController = Navigation.findNavController(getView());
                navHostController.popBackStack();
                KeyboardHelper.hideKeyboard(activity);
                break;
            default:
        }
        return false;
    }








}
