package com.watermelon.UI.Details;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.watermelon.Models.TvSeriesPicture;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class DetailsPicturesViewPagerAdapter extends PagerAdapter {

    private Context context;
    private List<TvSeriesPicture> pictures = new ArrayList<>();

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        ImageView imageView = new ImageView(context);
        TvSeriesPicture currentTvSeriesPicture = pictures.get(position);
        Picasso.get()
                .load(currentTvSeriesPicture.getPictureImagePath())
                .fit()
//                .centerCrop()
                .into(imageView);
        container.addView(imageView);
        return imageView;
    }
    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    public void setPictures(List<TvSeriesPicture> pictures) {
        this.pictures = pictures;
        notifyDataSetChanged();
    }

    public  void setContext(Context context){
        this.context = context;
    }

    @Override
    public int getCount() {
        if(pictures != null) {
            return pictures.size();
        }
        return 0;
    }
}
