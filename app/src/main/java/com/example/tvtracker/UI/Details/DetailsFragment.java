package com.example.tvtracker.UI.Details;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.text.Html;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.tvtracker.Models.TvShowFull;
import com.example.tvtracker.Helpers.StringHelper;
import com.example.tvtracker.Models.TvShowEpisode;
import com.example.tvtracker.Helpers.TvShowHelper;
import com.example.tvtracker.UI.MainActivity;
import com.example.tvtracker.Repository.AppRepoHelpClasses.Resource;
import com.example.tvtracker.Repository.AppRepoHelpClasses.Status;
import com.example.tvtracker.Models.TvShow;
import com.example.tvtracker.Models.TvShowSeason;
import com.example.tvtracker.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class DetailsFragment extends Fragment implements DetailsSeasonsAdapter.OnItemClickListener, View.OnClickListener {


    private Activity activity;
    private DetailsViewModel detailsViewModel;
    private int tvShowId;

    private DetailsSeasonsAdapter detailsSeasonsAdapter;
    private DetailsPicturesViewPagerAdapter detailsPicturesViewPagerAdapter;

    private NestedScrollView dataView;
    private RelativeLayout syncView;
    private TextView textViewName;
    private TextView textViewStatus;
    private TextView textViewDescription;
    private TextView textViewExpandDesc;
    private TextView textViewGenre;
    private TextView textViewRating;
    private ImageView imageViewImagePath;
    private TextView textViewCountry;
    private TextView textViewNetwork;
    private ImageView imageViewShowState;
    private RecyclerView recyclerViewSeasons;
    private ViewPager imagesViewPager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.activity = getActivity();
        detailsViewModel = new ViewModelProvider(this).get(DetailsViewModel.class);
        detailsSeasonsAdapter = new DetailsSeasonsAdapter();
        detailsPicturesViewPagerAdapter = new DetailsPicturesViewPagerAdapter();
        detailsPicturesViewPagerAdapter.setContext(getContext());
        tvShowId = Integer.parseInt(getArguments().getString(MainActivity.TVSHOW_ID));
        detailsSeasonsAdapter.setOnItemClickListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.details_fragment, container, false);
        setHasOptionsMenu(true);
        syncView = view.findViewById(R.id.details_sync_layout);
        dataView = view.findViewById(R.id.details_nested_scroll_view);
        textViewName = view.findViewById(R.id.details_text_view_tv_show_name);
        textViewStatus = view.findViewById(R.id.details_text_view_tv_show_status);
        textViewDescription = view.findViewById(R.id.details_text_view_tv_show_description);
        textViewExpandDesc = view.findViewById(R.id.details_text_view_expand_description);
        textViewGenre = view.findViewById(R.id.details_text_view_tv_show_genre);
        textViewRating = view.findViewById(R.id.details_text_view_tv_show_rating);
        imageViewImagePath = view.findViewById(R.id.details_image_view_image_thumbnail);
        textViewCountry = view.findViewById(R.id.details_text_view_tv_show_country);
        textViewNetwork = view.findViewById(R.id.details_text_view_tv_show_network);
        imageViewShowState = view.findViewById(R.id.details_text_view_tv_show_state);
        imageViewShowState.setOnClickListener(this);
        imagesViewPager = view.findViewById(R.id.details_view_pager_tv_show_images);
        recyclerViewSeasons = view.findViewById(R.id.details_recycler_view_tv_show_seasons);
        recyclerViewSeasons.setLayoutManager(new LinearLayoutManager(activity));
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        detailsViewModel.setTvShowId(tvShowId);
        recyclerViewSeasons.setAdapter(detailsSeasonsAdapter);
        imagesViewPager.setAdapter(detailsPicturesViewPagerAdapter);
        detailsViewModel.getDetailsObservable().observe(getViewLifecycleOwner(), new Observer<Resource<TvShowFull>>() {
            @Override
            public void onChanged(Resource<TvShowFull> tvShowFullResource) {
                if (tvShowFullResource.status == Status.LOADING) {
                    dataView.setVisibility(View.GONE);
                    syncView.setVisibility(View.VISIBLE);
                }
                if (tvShowFullResource.data != null && tvShowFullResource.status != Status.LOADING) {
                    syncView.setVisibility(View.GONE);
                    dataView.setVisibility(View.VISIBLE);

                    TvShowFull tvShowFull = tvShowFullResource.data;
                    TvShow tvShow = tvShowFull.tvShow;

                    Picasso.get()
                            .load(tvShow.getTvShowImagePath())
                            .fit()
                            .into(imageViewImagePath);

                    textViewName.setText(tvShow.getTvShowName());
                    textViewStatus.setText(tvShow.getTvShowStatus());
                    setImage(TvShowHelper.getTvShowWatchlistState(tvShowFull.tvShow));

                    textViewDescription.setText(Html.fromHtml(tvShow.getTvShowDesc()));
                    textViewExpandDesc.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (textViewDescription.getMaxLines() == 10) {
                                textViewDescription.setMaxLines(3);
                                textViewExpandDesc.setText(getResources().getString(R.string.details_expand_desc_more));
                            } else {
                                textViewDescription.setMaxLines(10);
                                textViewExpandDesc.setText(getResources().getString(R.string.details_expand_desc_less));
                            }
                        }
                    });

                    textViewRating.setText(StringHelper.getTvShowRatingString(tvShow.getTvShowRating()));
                    textViewGenre.setText(StringHelper.getGenresString(tvShowFull.genres));
                    textViewCountry.setText(tvShow.getTvShowCountry());
                    textViewNetwork.setText(tvShow.getTvShowNetwork());

                    if (tvShowFull.pictures.size() == 0) {
                        imagesViewPager.setVisibility(View.GONE);
                    } else {
                        detailsPicturesViewPagerAdapter.setPictures(tvShowFull.pictures);
                    }
                    if (tvShowFullResource.data.episodes != null) {
                        detailsSeasonsAdapter.setEpisodes(TvShowHelper.getTvShowSeasons(tvShowFull.episodes));
                    }
                }
            }

        });
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
    public void onCheckBoxClick(TvShowSeason season, boolean isCheckboxChecked) {
        List<Integer> ids = new ArrayList<>();
        for (TvShowEpisode episode : season.getEpisodes()) {
            ids.add(episode.getId());
        }
        Pair<List<Integer>, Boolean> params = new Pair<>(ids, isCheckboxChecked);
        detailsViewModel.changeAllSeasonsWatchedFlag(params);
    }

    @Override
    public void onClick(View view) {
        Pair<Integer, Boolean> params;
        int tag = (Integer) view.getTag();
        switch (tag) {
            case R.drawable.ic_check_black_24dp: {
                params = new Pair<>(tvShowId, MainActivity.TVSHOW_WATCHING_FLAG_NO);
                detailsViewModel.changeTvShowWatchedFlag(params);
                setImage(false);
                break;
            }
            case R.drawable.ic_close_black_24dp: {
                params = new Pair<>(tvShowId, MainActivity.TVSHOW_WATCHING_FLAG_YES);
                detailsViewModel.changeTvShowWatchedFlag(params);
                setImage(true);
            }
        }
    }

    private void setImage(boolean isWatched) {
        if (isWatched) {
            imageViewShowState.setTag(R.drawable.ic_check_black_24dp);
            imageViewShowState.setImageResource(R.drawable.ic_check_black_24dp);
        } else {
            imageViewShowState.setTag(R.drawable.ic_close_black_24dp);
            imageViewShowState.setImageResource(R.drawable.ic_close_black_24dp);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavController navHostController = Navigation.findNavController(getView());
                navHostController.popBackStack();
                break;
            default:
        }
        return false;
    }

}
