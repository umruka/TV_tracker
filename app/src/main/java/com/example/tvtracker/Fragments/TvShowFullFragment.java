package com.example.tvtracker.Fragments;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tvtracker.MainActivity;
import com.example.tvtracker.Models.QueryModels.TvShowFull;
import com.example.tvtracker.Models.TvShow;
import com.example.tvtracker.R;
import com.example.tvtracker.ViewModels.TvShowViewModel;
import com.squareup.picasso.Picasso;

import java.util.List;


public class TvShowFullFragment extends Fragment {


    private Activity activity;
    private TvShowViewModel tvShowViewModel;

    private TextView textViewId;
    private TextView textViewName;
    private TextView textViewStatus;
    private TextView textViewDescription;
    private TextView textViewYoutubeLink;
    private TextView textViewRating;
    private ImageView imageViewImagePath;
    private TextView textViewCountry;
    private TextView textViewNetwork;



    public TvShowFullFragment() {
    }

    public static TvShowFullFragment newInstance() {
        return new TvShowFullFragment();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View  view = inflater.inflate(R.layout.fragment_tv_show_full, container, false);

        textViewId = view.findViewById(R.id.text_view_full_tv_show_id);
        textViewName = view.findViewById(R.id.text_view_full_tv_show_name);
        textViewStatus = view.findViewById(R.id.text_view_full_tv_show_status);
        textViewDescription = view.findViewById(R.id.text_view_full_tv_show_description);
        textViewYoutubeLink = view.findViewById(R.id.text_view_full_tv_show_youtube_link);
        textViewRating = view.findViewById(R.id.text_view_full_tv_show_rating);
        imageViewImagePath = view.findViewById(R.id.text_view_full_image_thumbnail);
        textViewCountry = view.findViewById(R.id.text_view_full_tv_show_country);
        textViewNetwork = view.findViewById(R.id.text_view_full_tv_show_network);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.activity = getActivity();

        tvShowViewModel = new ViewModelProvider(this).get(TvShowViewModel.class);

        int id = Integer.parseInt(getArguments().getString(MainActivity.TVSHOW_ID));
//        List<TvShowFull> tvShowFulls = tvShowViewModel.getTvShowWithPicturesById(id);
//        TvShow tvShow = tvShowFulls.get(0).tvShow;
        TvShow tvShow = tvShowViewModel.getTvShowBasic(id);
        textViewId.setText(String.valueOf(tvShow.getTvShowId()));
        textViewName.setText(tvShow.getTvShowName());
        textViewStatus.setText(tvShow.getTvShowStatus());
        textViewDescription.setText(tvShow.getTvShowDesc());
        textViewYoutubeLink.setText(tvShow.getTvShowYoutubeLink());
        textViewRating.setText(tvShow.getTvShowRating());
        Picasso.get().load(tvShow.getTvShowImagePath()).into(imageViewImagePath);
        textViewCountry.setText(tvShow.getTvShowCountry());
        textViewNetwork.setText(tvShow.getTvShowNetwork());


    }


}
