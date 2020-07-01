package com.example.tvtracker.UI.Calendar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tvtracker.Models.CalendarTvShowEpisode;
import com.example.tvtracker.Helpers.DateHelper;
import com.example.tvtracker.Models.TvShow;
import com.example.tvtracker.Models.TvShowEpisode;
import com.example.tvtracker.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.TvShowPictureViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(CalendarTvShowEpisode calendarTvShowEpisode);
    }

    private List<CalendarTvShowEpisode> episodes = new ArrayList<>();
    private OnItemClickListener listener;

    @NonNull
    @Override
    public TvShowPictureViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.calendar_item, parent, false);

        return new TvShowPictureViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TvShowPictureViewHolder holder, int position) {
        Context context = holder.itemView.getContext();
        CalendarTvShowEpisode currentTvShowEpisode = episodes.get(position);
        Picasso.get()
                .load(currentTvShowEpisode.tvShow.getTvShowImagePath())
                .placeholder(R.drawable.image_loading_placeholder)
                .error(R.drawable.image_error_placeholder)
                .fit()
                .into(holder.imageViewImage);

        TvShow tvShow =  currentTvShowEpisode.tvShow;
        TvShowEpisode episode = currentTvShowEpisode.episode;
        holder.textViewName.setText(tvShow.getTvShowName());
        holder.textViewEpisodeNum.setText(context.getString(R.string.calendar_episode, episode.getEpisodeNum()));
        holder.textViewSeasonNum.setText(context.getString(R.string.calendar_season, episode.getEpisodeSeasonNum()));
        holder.textViewAirDate.setText(DateHelper.getDateString(episode.getEpisodeAirDate()));
        holder.textViewDaysLeft.setText(DateHelper.daysDifferenceFromCurrentDate(episode.getEpisodeAirDate()));

    }


    public void setTvShows(List<CalendarTvShowEpisode> episodes) {
        this.episodes = episodes;
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


    class TvShowPictureViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageViewImage;
        private TextView textViewName;
        private TextView textViewEpisodeNum;
        private TextView textViewSeasonNum;
        private TextView textViewAirDate;
        private TextView textViewDaysLeft;

        private TvShowPictureViewHolder(View itemView) {
            super(itemView);
            imageViewImage = itemView.findViewById(R.id.calendar_image);
            textViewName = itemView.findViewById(R.id.calendar_name);
            textViewEpisodeNum = itemView.findViewById(R.id.calendar_episodeNum);
            textViewSeasonNum = itemView.findViewById(R.id.calendar_seasonNum);
            textViewAirDate = itemView.findViewById(R.id.calendar_air_date);
            textViewDaysLeft = itemView.findViewById(R.id.calendar_days_left);

            itemView.setOnClickListener(new View.OnClickListener() {
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
