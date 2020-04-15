package com.example.tvtracker.WatchlistModel;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tvtracker.R;

import java.util.ArrayList;
import java.util.List;

public class WatchlistAdapter extends RecyclerView.Adapter<WatchlistAdapter.WatchlistViewHolder> {

    private List<Watchlist> watchlists = new ArrayList<>();

    @NonNull
    @Override
    public WatchlistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.watchlist_item,parent, false);
        return new WatchlistViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull WatchlistViewHolder holder, int position) {
        Watchlist currentWatchlist = watchlists.get(position);
        holder.textViewWatchlistTvShowId.setText(Integer.toString(currentWatchlist.getWatchlistTvShowId()));
    }

    @Override
    public int getItemCount() {
        return watchlists.size();
    }

    public void setWatchlists(List<Watchlist> watchlists) {
        this.watchlists = watchlists;
        notifyDataSetChanged();
    }

    class WatchlistViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewWatchlistTvShowId;

        private WatchlistViewHolder(View itemView){
            super(itemView);
            textViewWatchlistTvShowId = itemView.findViewById(R.id.text_view_watchlist_show_id);
        }
    }

}
