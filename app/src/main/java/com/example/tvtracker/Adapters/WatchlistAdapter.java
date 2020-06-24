package com.example.tvtracker.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tvtracker.Models.DateHelper;
import com.example.tvtracker.Models.QueryModels.TvShowFull;
import com.example.tvtracker.Models.TvShow;
import com.example.tvtracker.Models.TvShowEpisode;
import com.example.tvtracker.R;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;
import java.util.List;

public class WatchlistAdapter extends RecyclerView.Adapter<WatchlistAdapter.TvShowCombinedViewHolder>{

    public interface OnItemClickListener {
    void onItemClick(TvShowFull tvShowFull);
    void onButtonClick(TvShowFull tvShowFull);
    }


    private List<TvShowFull> tvShows = new ArrayList<>();
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

        Picasso.get()
                .load(currentTvShow.getTvShowImagePath())
                .error(R.drawable.image_error_placeholder)
                .placeholder(R.drawable.image_loading_placeholder)
                .fit()
                .into(holder.textViewTvShowImageThumbnail);
//        holder.textViewTvShowId.setText(Integer.toString(currentTvShow.getTvShowId()));
        holder.textViewTvShowName.setText(currentTvShow.getTvShowName());
        holder.textViewTvShowCountry.setText(currentTvShow.getTvShowCountry());
        holder.textViewTvShowNetwork.setText(currentTvShow.getTvShowNetwork());
        int progressMax = tvShows.get(position).getTvShowEpisodes().size();
        int watched = tvShows.get(position).getEpisodeProgress();

        holder.textViewTvShowEpisodeProgress.setMax(progressMax);

        holder.textViewTvShowEpisodeProgress.setProgress(watched);

        holder.textViewProgress.setText(progressMax - watched  + " remaining");

        if(tvShows.get(position).getTvShowEpisodes().size() == 0){
            holder.textViewEpisodeName.setText("no episodes avaible");
            holder.textViewEpisodeReleaseDate.setText("no episodes avaible");
        }else if(!tvShows.get(position).getTvShowState()) {
            TvShowEpisode tvShowEpisode = tvShows.get(position).getNextWatched();
            holder.textViewEpisodeName.setText(tvShowEpisode.getSeasonNum() + "x" + tvShowEpisode.getEpisodeNum()  + " " + tvShowEpisode.getEpisodeName());
            holder.textViewEpisodeReleaseDate.setText(DateHelper.toDateString(tvShowEpisode.getEpisodeAirDate()));
//        }
        } else if(currentTvShow.getTvShowStatus().equals("Ended")) {
            holder.textViewEpisodeName.setText("Series finished");
            holder.textViewEpisodeReleaseDate.setText("");
        }else {
            holder.textViewEpisodeName.setText("No more released episodes");
            holder.textViewEpisodeReleaseDate.setText("");
        }
        holder.textViewEpisodeWatched.setImageResource(R.drawable.ic_check_black_24dp);

    }



    public void setTvShows(List<TvShowFull> tvShows) {
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
//        private TextView textViewTvShowId;
        private ProgressBar textViewTvShowEpisodeProgress;
        private TextView textViewProgress;

        private TextView textViewEpisodeName;
        private TextView textViewEpisodeReleaseDate;
        private ImageView textViewEpisodeWatched;

        private TvShowCombinedViewHolder(View itemView) {
            super(itemView);
            textViewTvShowImageThumbnail = itemView.findViewById(R.id.watchlist_image_thumbnail);
            textViewTvShowName = itemView.findViewById(R.id.watchlist_tv_show_name);
//            textViewTvShowId = itemView.findViewById(R.id.text_view_watchlist_tv_show_id);
            textViewTvShowEpisodeProgress = itemView.findViewById(R.id.watchlist_episode_progress);
            textViewProgress = itemView.findViewById(R.id.watchlist_episode_progress_text);


            textViewTvShowCountry = itemView.findViewById(R.id.watchlist_tv_show_country);
            textViewTvShowNetwork = itemView.findViewById(R.id.text_view_watchlist_tv_show_network);

            textViewEpisodeName = itemView.findViewById(R.id.watchlist_episode_name);
            textViewEpisodeReleaseDate = itemView.findViewById(R.id.watchlist_episode_release_date);
            textViewEpisodeWatched = itemView.findViewById(R.id.episode_watch_button);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if(listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(tvShows.get(position));
                    }
                }
            });

            textViewEpisodeWatched.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if(listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onButtonClick(tvShows.get(position));
                    }
                }
            });


        }
    }
    }


