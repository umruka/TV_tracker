package com.example.tvtracker.UI.Details;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tvtracker.DTO.Models.TvShowPicture;
import com.example.tvtracker.R;

import java.util.ArrayList;
import java.util.List;

import com.squareup.picasso.Picasso;

public class DetailsPicturesAdapter extends RecyclerView.Adapter<DetailsPicturesAdapter.PicturesViewHolder> {


    private List<TvShowPicture> pictures = new ArrayList<>();


    @NonNull
    @Override
    public PicturesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_image, parent, false);

        return new PicturesViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PicturesViewHolder holder, int position) {
        TvShowPicture currentTvShowPicture = pictures.get(position);
        Picasso.get().setIndicatorsEnabled(true);
        Picasso.get().load(currentTvShowPicture.getTvShowPicturePath()).into(holder.image);
    }

    public void setPictures(List<TvShowPicture> pictures) {
        this.pictures = pictures;
        notifyDataSetChanged();
    }

    public List<TvShowPicture> getTvPicturesShown() {
        return pictures;
    }

    @Override
    public int getItemCount() {
        if(pictures != null) {
            return pictures.size();
        }
        return 0;
    }

    class PicturesViewHolder extends RecyclerView.ViewHolder {

        private ImageView image;

        private PicturesViewHolder(View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.picture);

        }

    }


}
