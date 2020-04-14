package com.example.tvtracker.TvShowModel;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.tvtracker.Models.JSON_RootElement;
import com.example.tvtracker.Models.TV_Show;
import com.example.tvtracker.Repository.TvShowRepository;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class TvShowViewModel extends AndroidViewModel {
    private TvShowRepository repository;
    private LiveData<List<TvShow>> allTvShows;

    public TvShowViewModel(@NonNull Application application){
        super(application);
        repository = new TvShowRepository(application);
        allTvShows = repository.getAllTvShows();
    }

    public LiveData<List<TvShow>> getAllTvShows() { return allTvShows;}

    public void insert(TvShow tvShow) { repository.insertTvShow(tvShow);}

    public void deleteAllTvShows() { repository.deleteAllTvShows(); }

    public TvShow getTvShow(int id) { return repository.getTvShowById(id); }

    private JSON_RootElement downloadDataFromURL(){
        Context context = getApplication().getApplicationContext();

        String webPage = "https://www.episodate.com/api/most-popular?page=1";
        JSON_RootElement jsonRootElement = new JSON_RootElement();
        try(InputStream is = new URL(webPage).openStream(); Reader reader = new InputStreamReader(is, StandardCharsets.UTF_8)) {
        Gson gson = new Gson();
        jsonRootElement = gson.fromJson(reader, JSON_RootElement.class);
        return jsonRootElement;
        }catch (IOException e){
            e.getMessage();
            return jsonRootElement;
        }
    }

    private void insertTvShows(JSON_RootElement data){
        for(int i=0; i < data.getTVShows().size();i++){
            TV_Show urlTvShow = data.getTVShows().get(i);

            int tvShowId = urlTvShow.getId();
            String tvShowName = urlTvShow.getName();
            String tvShowStatus = urlTvShow.getStatus();

            TvShow tvShow = new TvShow(tvShowName, tvShowStatus, tvShowId);

        repository.insertTvShow(tvShow);

        }
    }

    private void InsertTvShows(){
        JSON_RootElement data = downloadDataFromURL();
        insertTvShows(data);
    }

    public void syncTvShows(){
        InsertTvShows();
    }


}
