package com.watermelon.UI.Discover;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.watermelon.Models.TvSeries;
import com.watermelon.UI.WatermelonMainActivity;
import com.watermelon.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class DiscoverAdapter extends RecyclerView.Adapter<DiscoverAdapter.TvSeriesViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(TvSeries tvSeries);
    }


    private List<TvSeries> tvSeries = new ArrayList<>();
    private List<TvSeries> filteredTvSeries = new ArrayList<>();
    private OnItemClickListener listener;

    @NonNull
    @Override
    public TvSeriesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.discover_item, parent, false);

        return new TvSeriesViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TvSeriesViewHolder holder, int position) {
        TvSeries currentTvSeries = tvSeries.get(position);
        Picasso.get()
                .load(currentTvSeries.getTvSeriesImagePath())
                .error(R.drawable.image_error_placeholder)
                .placeholder(R.drawable.image_loading_placeholder)
                .fit()
                .into(holder.imageViewImage);
        holder.textViewName.setText(currentTvSeries.getTvSeriesName());
    }

    void setTvSeries(List<TvSeries> tvSeries) {
        this.tvSeries = tvSeries;
        filteredTvSeries.addAll(this.tvSeries);
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

    void filter(String charText, int type) {
        tvSeries.clear();
        if (type == WatermelonMainActivity.NETWORKS_CODE) {
            for (TvSeries tvSeries : filteredTvSeries) {
                if (tvSeries.getTvSeriesNetwork().contains(charText)) {
                    this.tvSeries.add(tvSeries);
                }
            }
        } else if (type == WatermelonMainActivity.STATUS_CODE) {
            for (TvSeries tvSeries : filteredTvSeries) {
                if (tvSeries.getTvSeriesStatus().contains(charText)) {
                    this.tvSeries.add(tvSeries);
                }
            }
        }
        notifyDataSetChanged();
    }

    class TvSeriesViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewName;
        private ImageView imageViewImage;

        private TvSeriesViewHolder(View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.text_view_tv_series_name);
            imageViewImage = itemView.findViewById(R.id.text_view_discover_image_thumbnail);

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
