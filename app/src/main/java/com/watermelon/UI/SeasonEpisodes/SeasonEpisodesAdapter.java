package com.watermelon.UI.SeasonEpisodes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.watermelon.Helpers.DateHelper;
import com.watermelon.Helpers.StringHelper;
import com.watermelon.Models.TvSeriesEpisode;
import com.watermelon.R;

import java.util.ArrayList;
import java.util.List;


public class SeasonEpisodesAdapter extends RecyclerView.Adapter<SeasonEpisodesAdapter.EpisodeViewHolder> {

    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(TvSeriesEpisode episode);
    }

    private List<TvSeriesEpisode> episodes = new ArrayList<>();


    @NonNull
    @Override
    public EpisodeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.episode_item, parent, false);

        return new EpisodeViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull EpisodeViewHolder holder, int position) {
        TvSeriesEpisode currentTvSeriesEpisode = episodes.get(position);
        holder.textViewName.setText(currentTvSeriesEpisode.getEpisodeName());
        holder.textViewEpisodeAndSeasonNum.setText("S" + StringHelper.addZero(currentTvSeriesEpisode.getEpisodeSeasonNum()) + "E" + StringHelper.addZero(currentTvSeriesEpisode.getEpisodeNum()));
        holder.textViewAirDate.setText(DateHelper.getDateString(currentTvSeriesEpisode.getEpisodeAirDate()));
        boolean episodeState = currentTvSeriesEpisode.isEpisodeWatched();
        if (episodeState) {
            holder.checkBoxWatchedFlag.setChecked(true);
        } else {
            holder.checkBoxWatchedFlag.setChecked(false);
        }
    }

    public void setEpisodes(List<TvSeriesEpisode> pictures) {
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