package com.example.tvtracker.UI.Calendar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tvtracker.DTO.Models.CalendarTvShowEpisode;
import com.example.tvtracker.DTO.Models.DateHelper;
import com.example.tvtracker.DTO.Models.TvShowEpisode;
import com.example.tvtracker.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.TvShowPictureViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(CalendarTvShowEpisode calendarTvShowEpisode);
    }

    private List<CalendarTvShowEpisode> calendarTvShowEpisodes = new ArrayList<>();
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
        CalendarTvShowEpisode currentTvShowEpisode = calendarTvShowEpisodes.get(position);

        Picasso.get()
                .load(currentTvShowEpisode.getTvShowImageThumbnail())
                .fit()
                .into(holder.imageViewImage);

        Context context = holder.itemView.getContext();
        TvShowEpisode episode = currentTvShowEpisode.getTvShowEpisode();

        holder.textViewName.setText(currentTvShowEpisode.getTvShowName());
        holder.textViewEpisodeNum.setText(context.getString(R.string.calendar_episode, episode.getEpisodeNum()));
        holder.textViewSeasonNum.setText(context.getString(R.string.calendar_season, episode.getSeasonNum()));
        holder.textViewAirDate.setText(DateHelper.toDateString(episode.getEpisodeAirDate()));
        holder.textViewDaysLeft.setText(DateHelper.daysDifferenceFromCurrentDate(episode.getEpisodeAirDate()));

    }


    public void setTvShows(List<CalendarTvShowEpisode> tvShowPictures) {
        this.calendarTvShowEpisodes = tvShowPictures;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (calendarTvShowEpisodes != null) {
            return calendarTvShowEpisodes.size();
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
                        listener.onItemClick(calendarTvShowEpisodes.get(position));
                    }
                }
            });

        }

    }


}
