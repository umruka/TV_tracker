package com.example.tvtracker.UI.SeasonEpisodes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tvtracker.Helpers.DateHelper;
import com.example.tvtracker.Helpers.StringHelper;
import com.example.tvtracker.Models.TvShowEpisode;
import com.example.tvtracker.R;

import java.util.ArrayList;
import java.util.List;


public class SeasonEpisodesAdapter extends RecyclerView.Adapter<SeasonEpisodesAdapter.EpisodeViewHolder> {

    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(TvShowEpisode episode);
    }

    private List<TvShowEpisode> episodes = new ArrayList<>();


    @NonNull
    @Override
    public EpisodeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.episode_item, parent, false);

        return new EpisodeViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull EpisodeViewHolder holder, int position) {
        TvShowEpisode currentTvShowEpisode = episodes.get(position);
        holder.textViewName.setText(currentTvShowEpisode.getEpisodeName());
        holder.textViewEpisodeAndSeasonNum.setText("S" + StringHelper.addZero(currentTvShowEpisode.getEpisodeSeasonNum()) + "E" + StringHelper.addZero(currentTvShowEpisode.getEpisodeNum()));
        holder.textViewAirDate.setText(DateHelper.getDateString(currentTvShowEpisode.getEpisodeAirDate()));
        boolean episodeState = currentTvShowEpisode.isEpisodeWatched();
        if (episodeState) {
            holder.checkBoxWatchedFlag.setChecked(true);
        } else {
            holder.checkBoxWatchedFlag.setChecked(false);
        }
    }

    public void setEpisodes(List<TvShowEpisode> pictures) {
        this.episodes = pictures;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (episodes != null) {
            return episodes.size();
        }
        return 0;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;

    }


    class EpisodeViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewName;
        private TextView textViewEpisodeAndSeasonNum;
        private TextView textViewAirDate;
        private CheckBox checkBoxWatchedFlag;

        private EpisodeViewHolder(View itemView) {
            super(itemView);

            textViewName = itemView.findViewById(R.id.name);
            textViewEpisodeAndSeasonNum = itemView.findViewById(R.id.episodeAndSeasonNum);
            textViewAirDate = itemView.findViewById(R.id.air_date);
            checkBoxWatchedFlag = itemView.findViewById(R.id.watched_flag);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(episodes.get(position));
                    }

                }
            });
            checkBoxWatchedFlag.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(episodes.get(position));
                    }
                }
            });


        }

    }


}