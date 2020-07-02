package com.watermelon.UI.Details;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.watermelon.Helpers.StringHelper;
import com.watermelon.Helpers.TvSeriesHelper;
import com.watermelon.Models.TvSeriesSeason;
import com.watermelon.R;


import java.util.ArrayList;
import java.util.List;

public class DetailsSeasonsAdapter extends RecyclerView.Adapter<DetailsSeasonsAdapter.SeasonsViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(TvSeriesSeason season);

        void onCheckBoxClick(TvSeriesSeason season, boolean isCheckBoxChecked);
    }

    private List<TvSeriesSeason> seasons = new ArrayList<>();
    private List<Integer> currentProgressList = new ArrayList<>();
    private List<Integer> maxProgressList = new ArrayList<>();
    private Context context;
    private OnItemClickListener listener;


    @NonNull
    @Override
    public SeasonsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.season_item, parent, false);

        return new SeasonsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SeasonsViewHolder holder, int position) {

        this.context = holder.itemView.getContext();
        TvSeriesSeason currentTvSeriesSeason = seasons.get(position);

        int seasonCurrentProgress = TvSeriesHelper.getEpisodeProgress(currentTvSeriesSeason.getEpisodes());
        int seasonMaxProgress = currentTvSeriesSeason.getEpisodes().size();
        currentProgressList.add(seasonCurrentProgress);
        maxProgressList.add(seasonMaxProgress);

        if (seasonCurrentProgress == seasonMaxProgress) {
            holder.checkBoxSeason.setChecked(true);
        } else {
            holder.checkBoxSeason.setChecked(false);
        }
        holder.textViewSeasonNumber.setText(context.getString(R.string.details_seasonNumber, currentTvSeriesSeason.getSeasonNum()));
        holder.progressBarSeason.setProgress(seasonCurrentProgress);
        holder.progressBarSeason.setMax(seasonMaxProgress);
        holder.textViewProgressText.setText(context.getString(R.string.details_seasonProgress, StringHelper.addZero(seasonCurrentProgress), StringHelper.addZero(seasonMaxProgress)));
    }

    public void setEpisodes(List<TvSeriesSeason> pictures) {
        this.seasons = pictures;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (seasons != null) {
            return seasons.size();
        }
        return 0;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;

    }

    class SeasonsViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewSeasonNumber;
        private CheckBox checkBoxSeason;
        private ProgressBar progressBarSeason;
        private TextView textViewProgressText;

        private SeasonsViewHolder(View itemView) {
            super(itemView);

            textViewSeasonNumber = itemView.findViewById(R.id.seasonNumber);
            progressBarSeason = itemView.findViewById(R.id.seasonProgress);
            textViewProgressText = itemView.findViewById(R.id.seasonProgressText);
            checkBoxSeason = itemView.findViewById(R.id.seasonCheckBox);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(seasons.get(position));
                    }
                }
            });

            checkBoxSeason.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        int seasonCurrentProgress = 0;
                        int seasonMaxProgress = maxProgressList.get(position);

                        if (checkBoxSeason.isChecked()) {
                            progressBarSeason.setProgress(seasonMaxProgress);
                            currentProgressList.set(position, seasonMaxProgress);
                        } else {
                            currentProgressList.set(position, seasonCurrentProgress);
                            progressBarSeason.setProgress(seasonCurrentProgress);
                        }

                        //update value
                        seasonCurrentProgress = currentProgressList.get(position);

                        textViewProgressText.setText(context.getString(R.string.details_seasonProgress, StringHelper.addZero(seasonCurrentProgress), StringHelper.addZero(seasonMaxProgress)));
                        listener.onCheckBoxClick(seasons.get(position), checkBoxSeason.isChecked());
                    }
                }
            });


        }

    }


}