package com.example.tvtracker.UI.Search;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tvtracker.Models.TvShow;
import com.example.tvtracker.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchTvShowViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(TvShow tvShow);
    }


    private List<TvShow> tvShows = new ArrayList<>();
    private OnItemClickListener listener;


    @NonNull
    @Override
    public SearchTvShowViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.search_item, parent, false);

        return new SearchTvShowViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchTvShowViewHolder holder, int position) {
        TvShow currentTvShow = tvShows.get(position);
        Picasso.get()
                .load(currentTvShow.getTvShowImagePath())
                .error(R.drawable.image_error_placeholder)
                .placeholder(R.drawable.image_loading_placeholder)
                .fit()
                .into(holder.imageViewTvShowImage);
        holder.textViewTvShowName.setText(currentTvShow.getTvShowName());
        holder.textViewTvShowStatus.setText(currentTvShow.getTvShowStatus());
        holder.textViewTvShowCountry.setText(currentTvShow.getTvShowCountry());
        holder.textViewTvShowNetwork.setText(currentTvShow.getTvShowNetwork());
    }

    void setTvShows(List<TvShow> tvShows) {
        this.tvShows = tvShows;
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

    class SearchTvShowViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageViewTvShowImage;
        private TextView textViewTvShowName;
        private TextView textViewTvShowStatus;
        private TextView textViewTvShowCountry;
        private TextView textViewTvShowNetwork;

        private SearchTvShowViewHolder(View itemView) {
            super(itemView);
            imageViewTvShowImage = itemView.findViewById(R.id.search_image_view_image_thumbnail);
            textViewTvShowName = itemView.findViewById(R.id.search_text_view_tv_show_name);
            textViewTvShowStatus = itemView.findViewById(R.id.search_text_view_tv_show_status);
            textViewTvShowCountry = itemView.findViewById(R.id.search_text_view_tv_show_country);
            textViewTvShowNetwork = itemView.findViewById(R.id.search_text_view_tv_show_network);

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
