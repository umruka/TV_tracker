package com.watermelon.UI.Calendar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.watermelon.Models.TvSeriesCalendarEpisode;
import com.watermelon.Helpers.DateHelper;
import com.watermelon.Models.TvSeries;
import com.watermelon.Models.TvSeriesEpisode;
import com.watermelon.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.TvSeriesPictureViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(TvSeriesCalendarEpisode tvSeriesCalendarEpisode);
    }

    private List<TvSeriesCalendarEpisode> episodes = new ArrayList<>();
    private OnItemClickListener listener;

    @NonNull
    @Override
    public TvSeriesPictureViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.calendar_item, parent, false);

        return new TvSeriesPictureViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TvSeriesPictureViewHolder holder, int position) {
        Context context = holder.itemView.getContext();
        TvSeriesCalendarEpisode currentTvSeriesEpisode = episodes.get(position);
        TvSeries tvSeries =  currentTvSeriesEpisode.getTvSeries();
        TvSeriesEpisode episode = currentTvSeriesEpisode.getEpisode();

        Picasso.get()
                .load(tvSeries.getTvSeriesImagePath())
                .placeholder(R.drawable.image_loading_placeholder)
                .error(R.drawable.image_error_placeholder)
                .fit()
                .into(holder.imageViewImage);

        holder.textViewName.setText(tvSeries.getTvSeriesName());
        holder.textViewEpisodeNum.setText(context.getString(R.string.calendar_episode, episode.getEpisodeNum()));
        holder.textViewSeasonNum.setText(context.getString(R.string.calendar_season, episode.getEpisodeSeasonNum()));
        holder.textViewAirDate.setText(DateHelper.getDateString(episode.getEpisodeAirDate()));
        holder.textViewDaysLeft.setText(DateHelper.daysDifferenceFromCurrentDate(episode.getEpisodeAirDate()));

    }


    void setTvSeries(List<TvSeriesCalendarEpisode> episodes) {
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


    class TvSeriesPictureViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageViewImage;
        private TextView textViewName;
        private TextView textViewEpisodeNum;
        private TextView textViewSeasonNum;
        private TextView textViewAirDate;
        private TextView textViewDaysLeft;

        private TvSeriesPictureViewHolder(View itemView) {
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
