package com.example.tvtracker.Fragments;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.tvtracker.Adapters.SeasonsAdapter;
import com.example.tvtracker.Adapters.PicturesAdapter;
import com.example.tvtracker.MainActivity;
import com.example.tvtracker.Models.Basic.Resource;
import com.example.tvtracker.Models.Basic.Status;
import com.example.tvtracker.Models.Params.UpdateTvShowEpisodeWatchedFlagParams;
import com.example.tvtracker.Models.QueryModels.TvShowTest;
import com.example.tvtracker.Models.TvShow;
import com.example.tvtracker.Models.TvShowEpisode;
import com.example.tvtracker.Models.TvShowSeason;
import com.example.tvtracker.R;
import com.example.tvtracker.ViewModels.DetailsViewModel;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;


public class DetailsFragment extends Fragment implements SeasonsAdapter.OnItemClickListener {


    private Activity activity;
    private DetailsViewModel detailsViewModel;
    private static final int NUMBER_OF_COLUMNS =  3;
    private static final boolean WATCHED_FLAG_YES = true;


    private NestedScrollView dataView;
    private RelativeLayout syncView;

    private TextView textViewId;
    private TextView textViewName;
    private TextView textViewStatus;
    private TextView textViewDescription;
    private TextView textViewYoutubeLink;
    private TextView textViewRating;
    private ImageView imageViewImagePath;
    private TextView imagePath;
    private TextView textViewCountry;
    private TextView textViewNetwork;

    private RecyclerView gridLayoutPictures;
    private RecyclerView seasons;



    public DetailsFragment() {
    }

    public static DetailsFragment newInstance() {
        return new DetailsFragment();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View  view = inflater.inflate(R.layout.details_fragment, container, false);

        syncView = view.findViewById(R.id.sync_view);
        dataView = view.findViewById(R.id.nested_scroll_view);

        textViewId = view.findViewById(R.id.details_id);
        textViewName = view.findViewById(R.id.details_name);
        textViewStatus = view.findViewById(R.id.details_status);
        textViewDescription = view.findViewById(R.id.details_description);
        textViewYoutubeLink = view.findViewById(R.id.details_youtube_link);
        textViewRating = view.findViewById(R.id.details_rating);
        imageViewImagePath = view.findViewById(R.id.details_image_thumbnail);
        imagePath = view.findViewById(R.id.details_image_path);
        textViewCountry = view.findViewById(R.id.details_country);
        textViewNetwork = view.findViewById(R.id.details_network);

        gridLayoutPictures = view.findViewById(R.id.details_pictures_grid);
        gridLayoutPictures.setLayoutManager(new GridLayoutManager(activity, NUMBER_OF_COLUMNS));
        gridLayoutPictures.setHasFixedSize(true);

        seasons = view.findViewById(R.id.seasons);
        seasons.setLayoutManager(new LinearLayoutManager(activity));

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.activity = getActivity();

        PicturesAdapter picturesAdapter = new PicturesAdapter();
        SeasonsAdapter seasonsAdapter = new SeasonsAdapter();


        detailsViewModel = new ViewModelProvider(this).get(DetailsViewModel.class);

        gridLayoutPictures.setAdapter(picturesAdapter);
        seasons.setAdapter(seasonsAdapter);


        int id = Integer.parseInt(getArguments().getString(MainActivity.TVSHOW_ID));
        detailsViewModel.getTvShowTestObservable().observe(getViewLifecycleOwner(), new Observer<Resource<TvShowTest>>() {
            @Override
            public void onChanged(Resource<TvShowTest> tvShowTestResource) {
                if(tvShowTestResource.status == Status.LOADING) {
                    dataView.setVisibility(View.GONE);
                    syncView.setVisibility(View.VISIBLE);
                }
                if(tvShowTestResource.data != null && tvShowTestResource.status != Status.LOADING && tvShowTestResource.data.getTvShow() != null) {
                    syncView.setVisibility(View.GONE);
                    dataView.setVisibility(View.VISIBLE);
                    TvShowTest tvShowTest = tvShowTestResource.data;
                    TvShow tvShow = tvShowTest.getTvShow();
                    textViewId.setText(String.valueOf(tvShow.getTvShowId()));
                    textViewName.setText(tvShow.getTvShowName());
                    textViewStatus.setText(tvShow.getTvShowStatus());
                    textViewDescription.setText(tvShow.getTvShowDesc());
                    textViewYoutubeLink.setText(tvShow.getTvShowYoutubeLink());
                    textViewRating.setText(tvShow.getTvShowRating());
                    Picasso.get().load(tvShow.getTvShowImagePath()).into(imageViewImagePath);
                    imagePath.setText(tvShow.getTvShowImagePath());
                    textViewCountry.setText(tvShow.getTvShowCountry());
                    textViewNetwork.setText(tvShow.getTvShowNetwork());
                    picturesAdapter.setPictures(tvShowTest.getTvShowPictures());
                    seasonsAdapter.setEpisodes(tvShowTest.getTvShowSeasons());
                }
                }

        });

        detailsViewModel.getDetails(id);
        seasonsAdapter.setOnItemClickListener(this);



//        List<TvShowFull> tvShowFulls = discoverViewModel.getTvShowWithPicturesById(id);
//        TvShow tvShow = tvShowFulls.get(0).tvShow;



    }

    @Override
    public void onItemClick(TvShowSeason season) {
        NavController navHostController = Navigation.findNavController(getView());
        if (navHostController.getCurrentDestination().getId() == R.id.fragment_details) {
            Bundle bundle = new Bundle();
            int id = Integer.parseInt(getArguments().getString(MainActivity.TVSHOW_ID));
            int seasonNum = season.getSeasonNum();
            bundle.putString(MainActivity.TVSHOW_ID, String.valueOf(id));
            bundle.putString(MainActivity.TVSHOW_SEASON_NUM, String.valueOf(seasonNum));
            navHostController.navigate(R.id.action_fragment_details_to_fragment_episodes, bundle);
        }
    }

}
