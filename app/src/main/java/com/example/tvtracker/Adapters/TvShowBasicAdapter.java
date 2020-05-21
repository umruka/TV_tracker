package com.example.tvtracker.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tvtracker.Models.TvShow;
import com.example.tvtracker.R;

import java.util.ArrayList;
import java.util.List;

public class TvShowBasicAdapter extends RecyclerView.Adapter<TvShowBasicAdapter.TvShowViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(TvShow tvShow);
        void onButtonClick(TvShow tvShow);
    }


    private List<TvShow> tvShows = new ArrayList<>();
    private OnItemClickListener listener;


    @NonNull
    @Override
    public TvShowViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tvshow_item, parent, false);

        return new TvShowViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TvShowViewHolder holder, int position) {
        TvShow currentTvShow = tvShows.get(position);
        holder.textViewTvShowName.setText(currentTvShow.getTvShowName());
        holder.textViewTvShowStatus.setText(currentTvShow.getTvShowStatus());
        holder.textViewTvShowId.setText(Integer.toString(currentTvShow.getTvShowId()));
    }

    public void setTvShows(List<TvShow> tvShows) {
        this.tvShows = tvShows;
        notifyDataSetChanged();
    }

    public List<TvShow> getTvShowsShown() {
        return tvShows;
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

    class TvShowViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewTvShowName;
        private TextView textViewTvShowStatus;
        private TextView textViewTvShowId;
        private Button buttonTvShowAdd;

        private TvShowViewHolder(View itemView) {
            super(itemView);
            textViewTvShowName = itemView.findViewById(R.id.text_view_tvshow_name);
            textViewTvShowStatus = itemView.findViewById(R.id.text_view_tvshow_status);
            textViewTvShowId = itemView.findViewById(R.id.text_view_tvshow_id);
            buttonTvShowAdd = itemView.findViewById(R.id.button_tv_show_add);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(tvShows.get(position));
                    }
                }
            });

            buttonTvShowAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onButtonClick(tvShows.get(position));
                    }
                }
            });
        }

    }


}
