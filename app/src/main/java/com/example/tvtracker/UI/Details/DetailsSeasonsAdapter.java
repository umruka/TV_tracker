package com.example.tvtracker.UI.Details;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tvtracker.DTO.Models.TvShowSeason;
import com.example.tvtracker.R;


import java.util.ArrayList;
import java.util.List;

public class DetailsSeasonsAdapter extends RecyclerView.Adapter<DetailsSeasonsAdapter.SeasonsViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(TvShowSeason season);
    }

    private List<TvShowSeason> seasons = new ArrayList<>();
    private OnItemClickListener listener;


    @NonNull
    @Override
    public SeasonsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_season, parent, false);

        return new SeasonsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SeasonsViewHolder holder, int position) {
        TvShowSeason currentTvShowSeason = seasons.get(position);

        int progress = currentTvShowSeason.getEpisodes().size();
        holder.progressBar.setMax(progress);
        holder.progressBar.setProgress(currentTvShowSeason.getSeasonProgress());

        holder.textView.setText("Season " +String.valueOf(currentTvShowSeason.getSeasonNum()));
        holder.progressTextView.setText(currentTvShowSeason.getSeasonProgress() + "/" + progress);
    }

    public void setEpisodes(List<TvShowSeason> pictures) {
        this.seasons = pictures;
        notifyDataSetChanged();
    }

    public List<TvShowSeason> getTvEpisodesShown() {
        return seasons;
    }

    @Override
    public int getItemCount() {
        if(seasons != null) {
            return seasons.size();
        }
        return 0;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;

    }


    class SeasonsViewHolder extends RecyclerView.ViewHolder {

        private TextView textView;
        private ProgressBar progressBar;
        private TextView progressTextView;

        private SeasonsViewHolder(View itemView) {
            super(itemView);

            textView = itemView.findViewById(R.id.seasonNumber);
            progressBar = itemView.findViewById(R.id.seasonProgress);
            progressTextView = itemView.findViewById(R.id.seasonProgressText);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(seasons.get(position));
                    }
                }
            });


        }

    }


}