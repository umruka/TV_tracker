package com.example.tvtracker.ViewModels;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.tvtracker.JsonModels.TvShowDetails.JsonTvShowDetailsRoot;
import com.example.tvtracker.JsonModels.TvShowDetails.JsonTvShowDetails;
import com.example.tvtracker.Models.TvShowDetails;
import com.example.tvtracker.Repository.AppRepository;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class TvShowDetailsViewModel extends AndroidViewModel {
    private AppRepository repository;
    private LiveData<List<TvShowDetails>> allTvShowsFull;

    public TvShowDetailsViewModel(@NonNull Application application) {
        super(application);
        repository = new AppRepository(application);
        allTvShowsFull = repository.getAllTvShowsFull();
    }

    public LiveData<List<TvShowDetails>> getAllTvShowsFull() {
        return allTvShowsFull;
    }

    public void insert(TvShowDetails tvShowDetails) {
        repository.insertTvShowFull(tvShowDetails);
    }

    public void insertTvShowDetails(int id){
        repository.getTvShowDetailInfo(id);
    }

    private JsonTvShowDetailsRoot downloadDataFromURL(int tvShowId) {
        Context context = getApplication().getApplicationContext();

        String webPage = "https://www.episodate.com/api/show-details?q=" + tvShowId;
        JsonTvShowDetailsRoot jsonRootElement = new JsonTvShowDetailsRoot();
        try (InputStream is = new URL(webPage).openStream(); Reader reader = new InputStreamReader(is, StandardCharsets.UTF_8)) {
            Gson gson = new Gson();
            jsonRootElement = gson.fromJson(reader, JsonTvShowDetailsRoot.class);
            return jsonRootElement;
        } catch (IOException e) {
            e.getMessage();
            return jsonRootElement;
        }
    }

    private void insertTvShowFull(JsonTvShowDetailsRoot data) {
        JsonTvShowDetails tvShowFull = data.getTvShow();

        int showId = tvShowFull.getId();
        String description = tvShowFull.getDescription();
        String youtubeLink = tvShowFull.getYoutubeLink();
        String rating = tvShowFull.getRating();
        String imagePath = tvShowFull.getImagePath();

        TvShowDetails newTvShowDetails = new TvShowDetails(showId, description, youtubeLink, rating, imagePath);
        insert(newTvShowDetails);


    }

    public void insertItem(int id){
    JsonTvShowDetailsRoot data =  downloadDataFromURL(id);
    insertTvShowFull(data);
    }

}
