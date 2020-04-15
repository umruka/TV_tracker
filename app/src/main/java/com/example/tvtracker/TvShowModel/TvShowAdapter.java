package com.example.tvtracker.TvShowModel;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tvtracker.R;

import java.util.ArrayList;
import java.util.List;

public class TvShowAdapter extends RecyclerView.Adapter<TvShowAdapter.TvShowViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(TvShow tvShow);
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

    public void setTvShows(List<TvShow> tvShows){
        this.tvShows = tvShows;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return tvShows.size();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;

    }

    class TvShowViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewTvShowName;
        private TextView textViewTvShowStatus;
        private TextView textViewTvShowId;

        private TvShowViewHolder(View itemView){
            super(itemView);
            textViewTvShowName = itemView.findViewById(R.id.text_view_tvshow_name);
            textViewTvShowStatus = itemView.findViewById(R.id.text_view_tvshow_status);
            textViewTvShowId = itemView.findViewById(R.id.text_view_tvshow_id);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(tvShows.get(position));
                    }
                }
            });
        }

    }




}
