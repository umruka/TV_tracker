package com.example.tvtracker.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tvtracker.Models.CalendarTvShowEpisode;
import com.example.tvtracker.Models.DateHelper;
import com.example.tvtracker.Models.TvShowPicture;
import com.example.tvtracker.R;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.TvShowPictureViewHolder> {




    private List<CalendarTvShowEpisode> calendarTvShowEpisodes = new ArrayList<>();


    @NonNull
    @Override
    public TvShowPictureViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_calendar, parent, false);

        return new TvShowPictureViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TvShowPictureViewHolder holder, int position) {
        CalendarTvShowEpisode currentTvShowEpisode = calendarTvShowEpisodes.get(position);
        Picasso.get().setIndicatorsEnabled(true);
        Picasso.get().load(currentTvShowEpisode.getTvShowImageThumbnail()).into(holder.imageViewImage);
        holder.textViewName.setText(currentTvShowEpisode.getTvShowName());
        holder.textViewEpisodeNum.setText(String.valueOf("Episode: " + currentTvShowEpisode.getTvShowEpisode().getEpisodeNum()));
        holder.textViewSeasonNum.setText(String.valueOf("Season: " + currentTvShowEpisode.getTvShowEpisode().getSeasonNum()));
        holder.textViewAirDate.setText(String.valueOf(currentTvShowEpisode.getTvShowEpisode().getEpisodeAirDate()));
        holder.textViewDaysLeft.setText(DateHelper.daysDifferenceFromCurrentDate(currentTvShowEpisode.getTvShowEpisode().getEpisodeAirDate()) + " days left");

    }



    public void setTvShows(List<CalendarTvShowEpisode> tvShowPictures) {
        this.calendarTvShowEpisodes = tvShowPictures;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if(calendarTvShowEpisodes != null) {
            return calendarTvShowEpisodes.size();
        }
        return 0;
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

        }

    }


}
