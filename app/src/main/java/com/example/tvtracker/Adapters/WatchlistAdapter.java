package com.example.tvtracker.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tvtracker.Models.QueryModels.TvShowTest;
import com.example.tvtracker.Models.TvShow;
import com.example.tvtracker.Models.TvShowEpisode;
import com.example.tvtracker.R;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;
import java.util.List;

public class WatchlistAdapter extends RecyclerView.Adapter<WatchlistAdapter.TvShowCombinedViewHolder>{

    public interface OnItemClickListener {
    void onItemClick(TvShowTest tvShowTest);
    }


    private List<TvShowTest> tvShows = new ArrayList<>();
    private OnItemClickListener listener;


    @NonNull
    @Override
    public TvShowCombinedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_watchlist, parent, false);

        return new TvShowCombinedViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TvShowCombinedViewHolder holder, int position) {
        TvShow currentTvShow = tvShows.get(position).getTvShow();

        Picasso.get().load(currentTvShow.getTvShowImagePath()).into(holder.textViewTvShowImageThumbnail);
//        holder.textViewTvShowId.setText(Integer.toString(currentTvShow.getTvShowId()));
        holder.textViewTvShowName.setText(currentTvShow.getTvShowName());
        int progressMax = tvShows.get(position).getTvShowEpisodes().size();
        int watched = tvShows.get(position).getEpisodeProgress();

        holder.textViewTvShowEpisodeProgress.setMax(progressMax);

        holder.textViewTvShowEpisodeProgress.setProgress(watched);

        holder.textViewProgress.setText(watched + "/" + progressMax);

        if(!tvShows.get(position).getTvShowState()) {
            TvShowEpisode tvShowEpisode = tvShows.get(position).getNextWatched();
            holder.textViewEpisodeName.setText(tvShowEpisode.getEpisodeName());
            holder.textViewEpisodeReleaseDate.setText(tvShowEpisode.getEpisodeAirDate());
//        }
        } else {
            holder.textViewEpisodeName.setText("Series finished");
            holder.textViewEpisodeReleaseDate.setText("");
        }

    }

    public void setTvShows(List<TvShowTest> tvShows) {
        this.tvShows = tvShows;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if(tvShows != null) {
            return tvShows.size();
        }
        return 0;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;

    }


    class TvShowCombinedViewHolder extends RecyclerView.ViewHolder {
        private ImageView textViewTvShowImageThumbnail;
        private TextView textViewTvShowName;
//        private TextView textViewTvShowId;
        private ProgressBar textViewTvShowEpisodeProgress;
        private TextView textViewProgress;

        private TextView textViewEpisodeName;
        private TextView textViewEpisodeReleaseDate;

        private TvShowCombinedViewHolder(View itemView) {
            super(itemView);
            textViewTvShowImageThumbnail = itemView.findViewById(R.id.text_view_watchlist_image_thumbnail);
            textViewTvShowName = itemView.findViewById(R.id.text_view_watchlist_tv_show_name);
//            textViewTvShowId = itemView.findViewById(R.id.text_view_watchlist_tv_show_id);
            textViewTvShowEpisodeProgress = itemView.findViewById(R.id.episode_progress);
            textViewProgress = itemView.findViewById(R.id.episode_progress_text);

            textViewEpisodeName = itemView.findViewById(R.id.episode_name);
            textViewEpisodeReleaseDate = itemView.findViewById(R.id.episode_release_date);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if(listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(tvShows.get(position));
                    }
                }
            });

        }
    }
    }


