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

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchTvSeriesViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(TvSeries tvSeries);
    }


    private List<TvSeries> tvSeries = new ArrayList<>();
    private OnItemClickListener listener;


    @NonNull
    @Override
    public SearchTvSeriesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.search_item, parent, false);

        return new SearchTvSeriesViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchTvSeriesViewHolder holder, int position) {
        TvSeries currentTvSeries = tvSeries.get(position);
        Picasso.get()
                .load(currentTvSeries.getTvSeriesImagePath())
                .error(R.drawable.image_error_placeholder)
                .placeholder(R.drawable.image_loading_placeholder)
                .fit()
                .into(holder.imageViewTvSeriesImage);
        holder.textViewTvSeriesName.setText(currentTvSeries.getTvSeriesName());
        holder.textViewTvSeriesStatus.setText(currentTvSeries.getTvSeriesStatus());
        holder.textViewTvSeriesCountry.setText(currentTvSeries.getTvSeriesCountry());
        holder.textViewTvSeriesNetwork.setText(currentTvSeries.getTvSeriesNetwork());
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

    class SearchTvSeriesViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageViewTvSeriesImage;
        private TextView textViewTvSeriesName;
        private TextView textViewTvSeriesStatus;
        private TextView textViewTvSeriesCountry;
        private TextView textViewTvSeriesNetwork;

        private SearchTvSeriesViewHolder(View itemView) {
            super(itemView);
            imageViewTvSeriesImage = itemView.findViewById(R.id.search_image_view_image_thumbnail);
            textViewTvSeriesName = itemView.findViewById(R.id.search_text_view_tv_series_name);
            textViewTvSeriesStatus = itemView.findViewById(R.id.search_text_view_tv_series_status);
            textViewTvSeriesCountry = itemView.findViewById(R.id.search_text_view_tv_series_country);
            textViewTvSeriesNetwork = itemView.findViewById(R.id.search_text_view_tv_series_network);

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
