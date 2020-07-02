package com.watermelon.UI.Search;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.watermelon.Models.TvSeries;
import com.watermelon.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchTvShowViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(TvSeries tvSeries);
    }


    private List<TvSeries> tvSeries = new ArrayList<>();
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
        TvSeries currentTvSeries = tvSeries.get(position);
        Picasso.get()
                .load(currentTvSeries.getTvShowImagePath())
                .error(R.drawable.image_error_placeholder)
                .placeholder(R.drawable.image_loading_placeholder)
                .fit()
                .into(holder.imageViewTvShowImage);
        holder.textViewTvShowName.setText(currentTvSeries.getTvShowName());
        holder.textViewTvShowStatus.setText(currentTvSeries.getTvShowStatus());
        holder.textViewTvShowCountry.setText(currentTvSeries.getTvShowCountry());
        holder.textViewTvShowNetwork.setText(currentTvSeries.getTvShowNetwork());
    }

    void setTvSeries(List<TvSeries> tvSeries) {
        this.tvSeries = tvSeries;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (tvSeries != null) {
            return tvSeries.size();
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
                        listener.onItemClick(tvSeries.get(position));
                    }
                }
            });

        }

    }


}
