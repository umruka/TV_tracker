package com.example.tvtracker.UI.Watchlist;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tvtracker.Helpers.DateHelper;
import com.example.tvtracker.Models.TvShowFull;
import com.example.tvtracker.Helpers.StringHelper;
import com.example.tvtracker.Models.TvShow;
import com.example.tvtracker.Models.TvShowEpisode;
import com.example.tvtracker.Helpers.TvShowHelper;
import com.example.tvtracker.R;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;
import java.util.List;

public class WatchlistAdapter extends RecyclerView.Adapter<WatchlistAdapter.TvShowCombinedViewHolder>{

    public interface OnItemClickListener {
    void onItemClick(TvShowFull tvShowFull);
    void onButtonClick(TvShowFull tvShowFull, int position);
    }

    private List<TvShowFull> tvShows = new ArrayList<>();
    private OnItemClickListener listener;

    @NonNull
    @Override
    public TvShowCombinedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.watchlist_item, parent, false);

        return new TvShowCombinedViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TvShowCombinedViewHolder holder, int position) {
        TvShow currentTvShow = tvShows.get(position).tvShow;
        int watched = TvShowHelper.getEpisodeProgress(tvShows.get(position).episodes);
        int progressMax = tvShows.get(position).episodes.size();

        Picasso.get()
                .load(currentTvShow.getTvShowImagePath())
                .error(R.drawable.image_error_placeholder)
                .placeholder(R.drawable.image_loading_placeholder)
                .fit()
                .into(holder.textViewTvShowImageThumbnail);

        holder.textViewProgress.setText(progressMax - watched  + " remaining");
        holder.textViewTvShowName.setText(currentTvShow.getTvShowName());
        holder.textViewTvShowCountry.setText(currentTvShow.getTvShowCountry());
        holder.textViewTvShowNetwork.setText(currentTvShow.getTvShowNetwork());
        holder.imageViewEpisodeWatched.setImageResource(R.drawable.ic_check_black_24dp);

        if(tvShows.get(position).episodes.size() == 0){
            holder.textViewEpisodeName.setText("no episodes avaible");
            holder.textViewEpisodeReleaseDate.setText("no episodes avaible");
        }else if(!TvShowHelper.getTvShowState(tvShows.get(position).episodes)) {
            TvShowEpisode tvShowEpisode = TvShowHelper.getNextWatched(tvShows.get(position).episodes);
            holder.textViewEpisodeName.setText(StringHelper.addZero(tvShowEpisode.getEpisodeSeasonNum()) + "x" + StringHelper.addZero(tvShowEpisode.getEpisodeNum())  + " " + tvShowEpisode.getEpisodeName());
            holder.textViewEpisodeReleaseDate.setText(DateHelper.getDateString(tvShowEpisode.getEpisodeAirDate()));
//        }
        }else {
            holder.textViewEpisodeName.setText("No more released episodes");
            holder.textViewEpisodeReleaseDate.setText("");
        }
        holder.textViewTvShowEpisodeProgress.setProgress(watched);
        holder.textViewTvShowEpisodeProgress.setMax(progressMax);

    }

    void setTvShows(List<TvShowFull> tvShows) {
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
        private TextView textViewTvShowCountry;
        private TextView textViewTvShowNetwork;
        private ProgressBar textViewTvShowEpisodeProgress;
        private TextView textViewProgress;
        private TextView textViewEpisodeName;
        private TextView textViewEpisodeReleaseDate;
        private ImageView imageViewEpisodeWatched;

        private TvShowCombinedViewHolder(View itemView) {
            super(itemView);
            textViewTvShowImageThumbnail = itemView.findViewById(R.id.watchlist_image_thumbnail);
            textViewProgress = itemView.findViewById(R.id.watchlist_episode_progress_text);
            textViewTvShowCountry = itemView.findViewById(R.id.watchlist_tv_show_country);
            textViewTvShowNetwork = itemView.findViewById(R.id.text_view_watchlist_tv_show_network);
            imageViewEpisodeWatched = itemView.findViewById(R.id.episode_watch_button);
            textViewTvShowName = itemView.findViewById(R.id.watchlist_tv_show_name);
            textViewEpisodeName = itemView.findViewById(R.id.watchlist_episode_name);
            textViewEpisodeReleaseDate = itemView.findViewById(R.id.watchlist_episode_release_date);
            textViewTvShowEpisodeProgress = itemView.findViewById(R.id.watchlist_episode_progress);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if(listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(tvShows.get(position));
                    }
                }
            });

            imageViewEpisodeWatched.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if(listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onButtonClick(tvShows.get(position), position);
                    }
                }
            });


        }
    }
    }


