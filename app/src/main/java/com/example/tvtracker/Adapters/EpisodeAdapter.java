package com.example.tvtracker.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tvtracker.Models.TvShowEpisode;
import com.example.tvtracker.Models.TvShowPicture;
import com.example.tvtracker.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class EpisodeAdapter extends RecyclerView.Adapter<EpisodeAdapter.EpisodeViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(int position, TvShowEpisode episode);
    }

    private List<TvShowEpisode> episodes = new ArrayList<>();
    private OnItemClickListener listener;
    private boolean episodeState;


    @NonNull
    @Override
    public EpisodeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_episode, parent, false);

        return new EpisodeViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull EpisodeViewHolder holder, int position) {
        TvShowEpisode currentTvShowEpisode = episodes.get(position);
        holder.textView.setText(currentTvShowEpisode.getEpisodeName());
        episodeState = currentTvShowEpisode.isWatched();
        if(episodeState) {
            holder.imageView.setImageResource(R.drawable.ic_check_black_24dp);
        }else {
            holder.imageView.setImageResource(R.drawable.ic_close_black_24dp);
        }
        }

    public void setEpisodes(List<TvShowEpisode> pictures) {
        this.episodes = pictures;
        notifyDataSetChanged();
    }

    public List<TvShowEpisode> getTvEpisodesShown() {
        return episodes;
    }

    @Override
    public int getItemCount() {
        if(episodes != null) {
            return episodes.size();
        }
        return 0;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;

    }


    class EpisodeViewHolder extends RecyclerView.ViewHolder {

        private TextView textView;
        private ImageView imageView;

        private EpisodeViewHolder(View itemView) {
            super(itemView);

            textView = itemView.findViewById(R.id.name);
            imageView = itemView.findViewById(R.id.watched_flag);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(position, episodes.get(position));
                    }

                }
            });


        }

    }


}