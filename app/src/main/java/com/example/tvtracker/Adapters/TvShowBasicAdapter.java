package com.example.tvtracker.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tvtracker.Models.TvShowBasic;
import com.example.tvtracker.R;

import java.util.ArrayList;
import java.util.List;

public class TvShowBasicAdapter extends RecyclerView.Adapter<TvShowBasicAdapter.TvShowViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(TvShowBasic tvShowBasic);
    }


    private List<TvShowBasic> tvShowBasics = new ArrayList<>();
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
        TvShowBasic currentTvShowBasic = tvShowBasics.get(position);
        holder.textViewTvShowName.setText(currentTvShowBasic.getTvShowName());
        holder.textViewTvShowStatus.setText(currentTvShowBasic.getTvShowStatus());
        holder.textViewTvShowId.setText(Integer.toString(currentTvShowBasic.getTvShowId()));
    }

    public void setTvShowBasics(List<TvShowBasic> tvShowBasics) {
        this.tvShowBasics = tvShowBasics;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return tvShowBasics.size();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;

    }

    class TvShowViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewTvShowName;
        private TextView textViewTvShowStatus;
        private TextView textViewTvShowId;

        private TvShowViewHolder(View itemView) {
            super(itemView);
            textViewTvShowName = itemView.findViewById(R.id.text_view_tvshow_name);
            textViewTvShowStatus = itemView.findViewById(R.id.text_view_tvshow_status);
            textViewTvShowId = itemView.findViewById(R.id.text_view_tvshow_id);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(tvShowBasics.get(position));
                    }
                }
            });
        }

    }


}
