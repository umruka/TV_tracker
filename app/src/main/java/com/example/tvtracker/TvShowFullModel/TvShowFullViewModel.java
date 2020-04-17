package com.example.tvtracker.TvShowFullModel;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.tvtracker.Models.TvShowFull.JSON_RootElementFull;
import com.example.tvtracker.Models.TvShowFull.TV_Show_Full;
import com.example.tvtracker.Repository.AppRepository;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class TvShowFullViewModel extends AndroidViewModel {
    private AppRepository repository;
    private LiveData<List<TvShowFull>> allTvShowsFull;

    public TvShowFullViewModel(@NonNull Application application) {
        super(application);
        repository = new AppRepository(application);
        allTvShowsFull = repository.getAllTvShowsFull();
    }

    public LiveData<List<TvShowFull>> getAllTvShowsFull() {
        return allTvShowsFull;
    }

    public void insert(TvShowFull tvShowFull) {
        repository.insertTvShowFull(tvShowFull);
    }

    private JSON_RootElementFull downloadDataFromURL(int tvShowId) {
        Context context = getApplication().getApplicationContext();

        String webPage = "https://www.episodate.com/api/show-details?q=" + tvShowId;
        JSON_RootElementFull jsonRootElement = new JSON_RootElementFull();
        try (InputStream is = new URL(webPage).openStream(); Reader reader = new InputStreamReader(is, StandardCharsets.UTF_8)) {
            Gson gson = new Gson();
            jsonRootElement = gson.fromJson(reader, JSON_RootElementFull.class);
            return jsonRootElement;
        } catch (IOException e) {
            e.getMessage();
            return jsonRootElement;
        }
    }

    private void insertTvShowFull(JSON_RootElementFull data) {
        TV_Show_Full tvShowFull = data.getTvShow();

        int showId = tvShowFull.getId();
        String description = tvShowFull.getDescription();
        String youtubeLink = tvShowFull.getYoutubeLink();
        String rating = tvShowFull.getRating();
        String imagePath = tvShowFull.getImagePath();

        TvShowFull newTvShowFull = new TvShowFull(showId, description, youtubeLink, rating, imagePath);
        insert(newTvShowFull);


    }

    public void insertItem(int id){
    JSON_RootElementFull data =  downloadDataFromURL(id);
    insertTvShowFull(data);
    }

}
