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
import com.example.tvtracker.ViewModels.TvShowViewModel;

import java.util.List;

import static com.example.tvtracker.MainActivity.TVSHOW_WATCHING_FLAG_YES;

public class SearchFragment extends Fragment {

    private TvShowViewModel tvShowViewModel;

    public static SearchFragment newInstance() {
        return new SearchFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.search_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        tvShowViewModel = new ViewModelProvider(this).get(TvShowViewModel.class);

        final RecyclerView recyclerView = getView().findViewById(R.id.search_recycler_view);
        recyclerView.setHasFixedSize(true);

        final TvShowBasicAdapter adapter = new TvShowBasicAdapter();
        recyclerView.setAdapter(adapter);
        tvShowViewModel.getAllSearchWordTvShows().observe(getViewLifecycleOwner(), new Observer<List<TvShow>>() {
            @Override
            public void onChanged(List<TvShow> tvShows) {
                adapter.setTvShows(tvShows);
            }
        });



        adapter.setOnItemClickListener(new TvShowBasicAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(TvShow tvShow) {

                tvShowViewModel.showSearch(tvShow);
//                Intent intent = new Intent(getContext(), TvShowFullActivity.class);
//                Bundle bundle = MainActivity.toBundle(tvShow);
//                intent.putExtra(MainActivity.TVSHOW_ID, tvShow.getTvShowId());
//                startActivity(intent);
                //MainActivity.toBundle(tvShowViewModel.getTvShowBasic(tvShow.getTvShowId()));
                NavController navHostController = Navigation.findNavController(getView());
                if(navHostController.getCurrentDestination().getId() == R.id.searchFragment){

                    Bundle bundle = new Bundle();
                    bundle.putString(MainActivity.TVSHOW_ID, String.valueOf(tvShow.getTvShowId()));
                    navHostController.navigate(R.id.action_searchFragment_to_tvShowFullFragment, bundle);
                }

            }

            @Override
            public void onButtonClick(TvShow tvShow) {
                tvShowViewModel.insertOrUpdate(tvShow);
                int id = tvShow.getTvShowId();
                UpdateTvShowWatchingFlagParams params = new UpdateTvShowWatchingFlagParams(id, TVSHOW_WATCHING_FLAG_YES);
                tvShowViewModel.updateTvShowBasicWatchingFlag(params);
//                tvShowViewModel.syncTvShowDetailsFromApi(id);

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
                tvShowViewModel.clearSearchedTvShows();
            }else{
                tvShowViewModel.searchWord(editable.toString(),1);
            }
            }
        });

        // TODO: Use the ViewModel
    }


}
