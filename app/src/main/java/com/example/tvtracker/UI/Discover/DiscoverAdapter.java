package com.example.tvtracker.UI.Discover;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tvtracker.Models.TvShow;
import com.example.tvtracker.UI.MainActivity;
import com.example.tvtracker.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class DiscoverAdapter extends RecyclerView.Adapter<DiscoverAdapter.TvShowViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(TvShow tvShow);
    }


    private List<TvShow> tvShows = new ArrayList<>();
    private List<TvShow> filteredTvShows = new ArrayList<>();
    private OnItemClickListener listener;

    @NonNull
    @Override
    public TvShowViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.discover_item, parent, false);

        return new TvShowViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TvShowViewHolder holder, int position) {
        TvShow currentTvShow = tvShows.get(position);
        Picasso.get()
                .load(currentTvShow.getTvShowImagePath())
                .error(R.drawable.image_error_placeholder)
                .placeholder(R.drawable.image_loading_placeholder)
                .fit()
                .into(holder.imageViewImage);
        holder.textViewName.setText(currentTvShow.getTvShowName());
    }

    void setTvShows(List<TvShow> tvShows) {
        this.tvShows = tvShows;
        filteredTvShows.addAll(this.tvShows);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (tvShows != null) {
            return tvShows.size();
        }
        return 0;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;

    }

    void filter(String charText, int type) {
        tvShows.clear();
        if (type == MainActivity.NETWORKS_CODE) {
            for (TvShow tvShow : filteredTvShows) {
                if (tvShow.getTvShowNetwork().contains(charText)) {
                    tvShows.add(tvShow);
                }
            }
        } else if (type == MainActivity.STATUS_CODE) {
            for (TvShow tvShow : filteredTvShows) {
                if (tvShow.getTvShowStatus().contains(charText)) {
                    tvShows.add(tvShow);
                }
            }
        }
        notifyDataSetChanged();
    }

    class TvShowViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewName;
        private ImageView imageViewImage;

        private TvShowViewHolder(View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.text_view_tvshow_name);
            imageViewImage = itemView.findViewById(R.id.text_view_discover_image_thumbnail);

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
