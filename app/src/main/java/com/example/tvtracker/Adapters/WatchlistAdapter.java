package com.example.tvtracker.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tvtracker.Models.TvShow;
import com.example.tvtracker.R;


import java.util.ArrayList;
import java.util.List;

public class WatchlistAdapter extends RecyclerView.Adapter<WatchlistAdapter.TvShowCombinedViewHolder> {


    private List<TvShow> tvShows = new ArrayList<>();


    @NonNull
    @Override
    public TvShowCombinedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.watchlist_item, parent, false);

        return new TvShowCombinedViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TvShowCombinedViewHolder holder, int position) {
        TvShow currentTvShow = tvShows.get(position);

        holder.textViewTvShowId.setText(Integer.toString(currentTvShow.getTvShowId()));
        holder.textViewTvShowName.setText(currentTvShow.getTvShowName());
        holder.textViewTvShowStatus.setText(currentTvShow.getTvShowStatus());
        holder.textViewTvShowDesc.setText(currentTvShow.getTvShowDesc());
        holder.textViewTvShowYoutubeLink.setText(currentTvShow.getTvShowYoutubeLink());holder.textViewTvShowRating.setText(currentTvShow.getTvShowRating());
        holder.textViewTvShowImagePath.setText(currentTvShow.getTvShowImagePath());
        holder.textViewTvShowCountry.setText(currentTvShow.getTvShowCountry());
        holder.textViewTvShowNetwork.setText(currentTvShow.getTvShowNetwork());
    }

    public void setTvShows(List<TvShow> tvShows) {
        this.tvShows = tvShows;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return tvShows.size();
    }
    class TvShowCombinedViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewTvShowName;
        private TextView textViewTvShowDesc;
        private TextView textViewTvShowCountry;
        private TextView textViewTvShowNetwork;
        private TextView textViewTvShowStatus;
        private TextView textViewTvShowYoutubeLink;
        private TextView textViewTvShowRating;
        private TextView textViewTvShowImagePath;
        private TextView textViewTvShowId;

        private TvShowCombinedViewHolder(View itemView) {
            super(itemView);
            textViewTvShowName = itemView.findViewById(R.id.text_view_tv_show_name);
            textViewTvShowStatus = itemView.findViewById(R.id.text_view_tv_show_status);
            textViewTvShowId = itemView.findViewById(R.id.text_view_tv_show_id);
            textViewTvShowDesc = itemView.findViewById(R.id.text_view_tv_show_description);
            textViewTvShowCountry = itemView.findViewById(R.id.text_view_tv_show_country);
            textViewTvShowNetwork = itemView.findViewById(R.id.text_view_tv_show_network);
            textViewTvShowYoutubeLink = itemView.findViewById(R.id.text_view_tv_show_youtube_link);
            textViewTvShowImagePath = itemView.findViewById(R.id.text_view_tv_show_image_path);
            textViewTvShowRating = itemView.findViewById(R.id.text_view_tv_show_rating);


        }
    }
    }


