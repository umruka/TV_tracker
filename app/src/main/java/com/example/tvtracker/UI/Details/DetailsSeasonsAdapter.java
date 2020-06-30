package com.example.tvtracker.UI.Details;

import android.content.Context;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tvtracker.DTO.Models.StringHelper;
import com.example.tvtracker.DTO.Models.TvShowSeason;
import com.example.tvtracker.MainActivity;
import com.example.tvtracker.R;


import java.util.ArrayList;
import java.util.List;

public class DetailsSeasonsAdapter extends RecyclerView.Adapter<DetailsSeasonsAdapter.SeasonsViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(TvShowSeason season);
        void onCheckBoxClick(TvShowSeason season, boolean isCheckBoxChecked);
    }

    private List<TvShowSeason> seasons = new ArrayList<>();
    private List<Integer> seasonCurrentProgressList = new ArrayList<>();
    private List<Integer> seasonMaxProgressList = new ArrayList<>();
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
        TvShowSeason currentTvShowSeason = seasons.get(position);

        int seasonCurrentProgress = currentTvShowSeason.getSeasonProgress();
        int seasonMaxProgress = currentTvShowSeason.getEpisodes().size();

        seasonCurrentProgressList.add(seasonCurrentProgress);
        seasonMaxProgressList.add(seasonMaxProgress);

        holder.progressBar.setMax(seasonMaxProgress);
        holder.progressBar.setProgress(seasonCurrentProgress);
        if (seasonCurrentProgress == seasonMaxProgress) {
            holder.checkBox.setChecked(true);
        }else{
            holder.checkBox.setChecked(false);
        }
        holder.textView.setText(context.getString(R.string.details_seasonNumber,currentTvShowSeason.getSeasonNum()));
        holder.progressTextView.setText(context.getString(R.string.details_seasonProgress, StringHelper.addZero(seasonCurrentProgress), StringHelper.addZero(seasonMaxProgress)));
    }

    public void setEpisodes(List<TvShowSeason> pictures) {
        this.seasons = pictures;
        notifyDataSetChanged();
    }

    public List<TvShowSeason> getTvEpisodesShown() {
        return seasons;
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

        private TextView textView;
        private CheckBox checkBox;
        private ProgressBar progressBar;
        private TextView progressTextView;

        private SeasonsViewHolder(View itemView) {
            super(itemView);

            textView = itemView.findViewById(R.id.seasonNumber);
            progressBar = itemView.findViewById(R.id.seasonProgress);
            progressTextView = itemView.findViewById(R.id.seasonProgressText);
            checkBox = itemView.findViewById(R.id.seasonCheckBox);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(seasons.get(position));
                    }
                }
            });

            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        int seasonCurrentProgress = 0;
                        int seasonMaxProgress = seasonMaxProgressList.get(position);

                        if(checkBox.isChecked()) {
                            progressBar.setProgress(seasonMaxProgress);
                            seasonCurrentProgressList.set(position, seasonMaxProgress);

                        }else{
                            seasonCurrentProgressList.set(position, seasonCurrentProgress);
                            progressBar.setProgress(seasonCurrentProgress);
                        }

                        //update value
                        seasonCurrentProgress = seasonCurrentProgressList.get(position);

                        progressTextView.setText(context.getString(R.string.details_seasonProgress, StringHelper.addZero(seasonCurrentProgress), StringHelper.addZero(seasonMaxProgress)));
                        listener.onCheckBoxClick(seasons.get(position), checkBox.isChecked());
                    }
                }
            });


        }

    }


}