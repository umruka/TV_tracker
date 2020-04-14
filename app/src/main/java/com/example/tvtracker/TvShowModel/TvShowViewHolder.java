package com.example.tvtracker.TvShowModel;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.tvtracker.R;

public class TvShowViewHolder extends RecyclerView.ViewHolder {
    public TextView textViewTvShowName;
    public TextView textViewTvShowStatus;
    public TextView textViewTvShowId;

    public TvShowViewHolder(View itemView){
        super(itemView);
        textViewTvShowName = itemView.findViewById(R.id.text_view_tvshow_name);
        textViewTvShowStatus = itemView.findViewById(R.id.text_view_tvshow_status);
        textViewTvShowId = itemView.findViewById(R.id.text_view_tvshow_id);
    }

}
