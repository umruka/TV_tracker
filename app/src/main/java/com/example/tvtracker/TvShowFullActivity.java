package com.example.tvtracker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

public class TvShowFullActivity extends AppCompatActivity {

    private TextView textViewId;
    private TextView textViewName;
    private TextView textViewStatus;
    private TextView textViewDescription;
    private TextView textViewYoutubeLink;
    private TextView textViewRating;
    private ImageView imageViewImagePath;
    private TextView textViewCountry;
    private TextView textViewNetwork;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tv_show_full);


        textViewId = findViewById(R.id.text_view_full_tv_show_id);
        textViewName = findViewById(R.id.text_view_full_tv_show_name);
        textViewStatus = findViewById(R.id.text_view_full_tv_show_status);
        textViewDescription = findViewById(R.id.text_view_full_tv_show_description);
        textViewYoutubeLink = findViewById(R.id.text_view_full_tv_show_youtube_link);
        textViewRating = findViewById(R.id.text_view_full_tv_show_rating);
        imageViewImagePath = findViewById(R.id.text_view_full_image_thumbnail);
        textViewCountry = findViewById(R.id.text_view_full_tv_show_country);
        textViewNetwork = findViewById(R.id.text_view_full_tv_show_network);


        Bundle bundle = this.getIntent().getBundleExtra(MainActivity.TVSHOW_BUNDLE);
        if(bundle != null) {
            String tvShowId = bundle.getString(MainActivity.TVSHOW_ID);
            String tvShowName = bundle.getString(MainActivity.TVSHOW_NAME);
            String tvShowStatus = bundle.getString(MainActivity.TVSHOW_STATUS);
            String tvShowDescription = bundle.getString(MainActivity.TVSHOW_DESCRIPTION);
            String tvShowYoutubeLink = bundle.getString(MainActivity.TVSHOW_YOUTUBE_LINK);
            String tvShowRating = bundle.getString(MainActivity.TVSHOW_RATING);
            String tvShowImagePath = bundle.getString(MainActivity.TVSHOW_IMAGE_PATH);
            String tvShowCountry = bundle.getString(MainActivity.TVSHOW_COUNTRY);
            String tvShowNetwork = bundle.getString(MainActivity.TVSHOW_NETWORK);


            textViewId.setText(tvShowId);
            textViewName.setText(tvShowName);
            textViewStatus.setText(tvShowStatus);
            textViewDescription.setText(tvShowDescription);
            textViewYoutubeLink.setText(tvShowYoutubeLink);
            textViewRating.setText(tvShowRating);
            Picasso.get().load(tvShowImagePath).into(imageViewImagePath);
            textViewCountry.setText(tvShowCountry);
            textViewNetwork.setText(tvShowNetwork);

        }else {
            Toast.makeText(this, "Problem", Toast.LENGTH_SHORT).show();
        }



    }
}
