package com.example.tvtracker.TvShowModel;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tvtracker.R;

import java.util.ArrayList;
import java.util.List;

public class TvShowAdapter extends RecyclerView.Adapter<TvShowViewHolder> {
    private List<TvShow> tvShows = new ArrayList<>();

    @NonNull
    @Override
    public TvShowViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tvshow_item, parent, false);

        return new TvShowViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TvShowViewHolder holder, int position) {
        TvShow currentTvShow = tvShows.get(position);
        holder.textViewTvShowName.setText(currentTvShow.getTvShowName());
        holder.textViewTvShowStatus.setText(currentTvShow.getTvShowStatus());
        holder.textViewTvShowId.setText(Integer.toString(currentTvShow.getTvShowId()));
    }

    public void setTvShows(List<TvShow> tvShows){
        this.tvShows = tvShows;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return tvShows.size();
    }
}
