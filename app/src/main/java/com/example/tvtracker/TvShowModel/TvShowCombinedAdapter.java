package com.example.tvtracker.TvShowModel;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tvtracker.R;
import com.example.tvtracker.Models.TvShowCombined;


import java.util.ArrayList;
import java.util.List;

public class TvShowCombinedAdapter extends RecyclerView.Adapter<TvShowCombinedAdapter.TvShowCombinedViewHolder> {


    private List<TvShowCombined> tvShowsCombined = new ArrayList<>();


    @NonNull
    @Override
    public TvShowCombinedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.watchlist_item, parent, false);

        return new TvShowCombinedViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TvShowCombinedViewHolder holder, int position) {
        TvShowCombined currentTvShow = tvShowsCombined.get(position);
        holder.textViewTvShowName.setText(currentTvShow.getTvShow().getTvShowNetwork());
        holder.textViewTvShowStatus.setText(currentTvShow.getTvShow().getTvShowStatus());
        holder.textViewTvShowId.setText(Integer.toString(currentTvShow.getTvShow().getTvShowId()));
        holder.textViewTvShowDesc.setText(currentTvShow.getTvShowFull().get(0).getTvShowDesc());
        holder.textViewTvShowCountry.setText(currentTvShow.getTvShow().getTvShowCountry());
        holder.textViewTvShowNetwork.setText(currentTvShow.getTvShow().getTvShowNetwork());
    }

    public void setTvShows(List<TvShowCombined> tvShows) {
        this.tvShowsCombined = tvShows;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return tvShowsCombined.size();
    }
    class TvShowCombinedViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewTvShowName;
        private TextView textViewTvShowDesc;
        private TextView textViewTvShowCountry;
        private TextView textViewTvShowNetwork;
        private TextView textViewTvShowStatus;
        private TextView textViewTvShowId;

        private TvShowCombinedViewHolder(View itemView) {
            super(itemView);
            textViewTvShowName = itemView.findViewById(R.id.text_view_tv_show_name);
            textViewTvShowStatus = itemView.findViewById(R.id.text_view_tv_show_status);
            textViewTvShowId = itemView.findViewById(R.id.text_view_tv_show_id);
            textViewTvShowDesc = itemView.findViewById(R.id.text_view_tv_show_description);
            textViewTvShowCountry = itemView.findViewById(R.id.text_view_tv_show_country);
            textViewTvShowNetwork = itemView.findViewById(R.id.text_view_tv_show_network);

        }
    }
    }


