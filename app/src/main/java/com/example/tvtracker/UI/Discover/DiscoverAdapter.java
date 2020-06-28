package com.example.tvtracker.UI.Discover;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tvtracker.DTO.Models.TvShow;
import com.example.tvtracker.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class DiscoverAdapter extends RecyclerView.Adapter<DiscoverAdapter.TvShowViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(TvShow tvShow);
//        void onButtonClick(TvShow tvShow);
    }


    private List<TvShow> tvShows = new ArrayList<>();
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
        holder.textViewTvShowName.setText(currentTvShow.getTvShowName());
//        holder.textViewTvShowStatus.setText(currentTvShow.getTvShowStatus());
//        holder.textViewTvShowId.setText(Integer.toString(currentTvShow.getTvShowId()));
            Picasso.get()
                    .load(currentTvShow.getTvShowImagePath())
                    .error(R.drawable.image_error_placeholder)
                    .placeholder(R.drawable.image_loading_placeholder)
                    .fit()
                    .into(holder.imageViewTvShowImage);
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
        private ImageView imageViewTvShowImage;

        private TvShowViewHolder(View itemView) {
            super(itemView);
            textViewTvShowName = itemView.findViewById(R.id.text_view_tvshow_name);
            imageViewTvShowImage = itemView.findViewById(R.id.text_view_discover_image_thumbnail);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(tvShows.get(position));
                    }
                }
            });

//            imageViewTvShowImage.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    int position = getAdapterPosition();
//                    if (listener != null && position != RecyclerView.NO_POSITION) {
//                        listener.onButtonClick(tvShows.get(position));
//                    }
//                }
//            });
        }

    }


}
