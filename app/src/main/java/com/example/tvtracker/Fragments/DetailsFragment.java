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

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toolbar;

import com.example.tvtracker.Adapters.SeasonsAdapter;
import com.example.tvtracker.Adapters.PicturesAdapter;
import com.example.tvtracker.MainActivity;
import com.example.tvtracker.Models.Basic.Resource;
import com.example.tvtracker.Models.Basic.Status;
import com.example.tvtracker.Models.Params.UpdateTvShowWatchingFlagParams;
import com.example.tvtracker.Models.QueryModels.TvShowFull;
import com.example.tvtracker.Models.TvShow;
import com.example.tvtracker.Models.TvShowSeason;
import com.example.tvtracker.R;
import com.example.tvtracker.ViewModels.DetailsViewModel;
import com.squareup.picasso.Picasso;


public class DetailsFragment extends Fragment implements SeasonsAdapter.OnItemClickListener, View.OnClickListener {


    private Activity activity;
    private DetailsViewModel detailsViewModel;
    private static final int NUMBER_OF_COLUMNS =  3;
    private static final boolean WATCHED_FLAG_YES = true;
    private int mId;


    private NestedScrollView dataView;
    private RelativeLayout syncView;

    private TextView textViewId;
    private TextView textViewName;
    private TextView textViewStatus;
    private TextView textViewDescription;
    private TextView textViewExpandDesc;
    private TextView textViewYoutubeLink;
    private TextView textViewRating;
    private ImageView imageViewImagePath;
    private TextView imagePath;
    private TextView textViewCountry;
    private TextView textViewNetwork;

    private ImageView imageViewShowState;
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
        textViewExpandDesc = view.findViewById(R.id.expand_desc);
        textViewYoutubeLink = view.findViewById(R.id.details_youtube_link);
        textViewRating = view.findViewById(R.id.details_rating);
        imageViewImagePath = view.findViewById(R.id.details_image_thumbnail);
        imagePath = view.findViewById(R.id.details_image_path);
        textViewCountry = view.findViewById(R.id.details_country);
        textViewNetwork = view.findViewById(R.id.details_network);
        imageViewShowState = view.findViewById(R.id.details_show_state);
        imageViewShowState.setOnClickListener(this::onClick);

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


        mId  = Integer.parseInt(getArguments().getString(MainActivity.TVSHOW_ID));
        detailsViewModel.setTvShowId(mId);
        detailsViewModel.getDetailsObservable().observe(getViewLifecycleOwner(), new Observer<Resource<TvShowFull>>() {
            @Override
            public void onChanged(Resource<TvShowFull> tvShowTestResource) {
                if(tvShowTestResource.status == Status.LOADING) {
                    dataView.setVisibility(View.GONE);
                    syncView.setVisibility(View.VISIBLE);
                }
                if(tvShowTestResource.data != null && tvShowTestResource.status != Status.LOADING && tvShowTestResource.data.getTvShow() != null) {
                    syncView.setVisibility(View.GONE);
                    dataView.setVisibility(View.VISIBLE);
                    TvShowFull tvShowFull = tvShowTestResource.data;
                    TvShow tvShow = tvShowFull.getTvShow();
                    textViewId.setText(String.valueOf(tvShow.getTvShowId()));
                    textViewName.setText(tvShow.getTvShowName());
                    textViewStatus.setText(tvShow.getTvShowStatus());
                    textViewDescription.setText(Html.fromHtml(tvShow.getTvShowDesc()));
//                    imageViewExpandDesc.setImageResource(R.drawable.ic_check_black_24dp);
                    textViewExpandDesc.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(textViewDescription.getMaxLines() == 10){
                                textViewDescription.setMaxLines(3);
                            }else {
                                textViewDescription.setMaxLines(10);
                            }
                        }
                    });
                    textViewYoutubeLink.setText(tvShow.getTvShowYoutubeLink());
                    textViewRating.setText(tvShow.getTvShowRating());
                    Picasso.get()
                            .load(tvShow.getTvShowImagePath())
                            .fit()
                            .into(imageViewImagePath);
                    imagePath.setText(tvShow.getTvShowImagePath());
                    textViewCountry.setText(tvShow.getTvShowCountry());
                    textViewNetwork.setText(tvShow.getTvShowNetwork());
                    picturesAdapter.setPictures(tvShowFull.getTvShowPictures());
                    if(tvShowTestResource.data.getTvShowEpisodes() != null) {
                        seasonsAdapter.setEpisodes(tvShowFull.getTvShowSeasons());
                    }
                    setImage(detailsViewModel.getShowState());
                }
                }

        });
//        detailsViewModel.getDetails2(mId);
//        detailsViewModel.getDetails(mId);
        seasonsAdapter.setOnItemClickListener(this::onItemClick);


//        List<fromDbCall> tvShowFulls = discoverViewModel.getTvShowWithPicturesById(id);
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

    @Override
    public void onClick(View view) {
        UpdateTvShowWatchingFlagParams params = new UpdateTvShowWatchingFlagParams(mId, false);
        int tag = (Integer) view.getTag();
        switch (tag){
            case R.drawable.ic_check_black_24dp:{
                params.setFlag(MainActivity.TVSHOW_WATCHING_FLAG_NO);
                detailsViewModel.setTvShowWatchedFlag(params);
                setImage(false);
                break;
            }
            case R.drawable.ic_close_black_24dp:{
                params.setFlag(MainActivity.TVSHOW_WATCHING_FLAG_YES);
                detailsViewModel.setTvShowWatchedFlag(params);
                setImage(true);
            }
        }
    }

    private void setImage(boolean isWatched){
        if(isWatched){
            imageViewShowState.setTag(R.drawable.ic_check_black_24dp);
            imageViewShowState.setImageResource(R.drawable.ic_check_black_24dp);
        }else{
            imageViewShowState.setTag(R.drawable.ic_close_black_24dp);
            imageViewShowState.setImageResource(R.drawable.ic_close_black_24dp);
        }
    }

}
