package com.example.tvtracker.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tvtracker.Models.TvShowPicture;
import com.example.tvtracker.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.TvShowPictureViewHolder> {




    private List<TvShowPicture> tvShowPictures = new ArrayList<>();


    @NonNull
    @Override
    public TvShowPictureViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_calendar, parent, false);

        return new TvShowPictureViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TvShowPictureViewHolder holder, int position) {
        TvShowPicture currentTvShowPicture = tvShowPictures.get(position);
        Picasso.get().setIndicatorsEnabled(true);
        Picasso.get().load(currentTvShowPicture.getTvShowPicturePath()).into(holder.imageView);
    }

    public void setTvShows(List<TvShowPicture> tvShowPictures) {
        this.tvShowPictures = tvShowPictures;
        //notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return tvShowPictures.size();
    }



    class TvShowPictureViewHolder extends RecyclerView.ViewHolder {
        private  ImageView imageView;
        private TvShowPictureViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.tv_show_picture);

        }

    }


}
