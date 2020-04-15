package com.example.tvtracker.TvShowModel;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.tvtracker.Models.JSON_RootElement;
import com.example.tvtracker.Models.TV_Show;
import com.example.tvtracker.Repository.TvShowRepository;
import com.example.tvtracker.Models.UpdateTvShowWatchingFlagParams;
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

    public void updateTvShow(TvShow tvShow) { repository.updateTvShow(tvShow);}

    public void updateTvShowWatchingFlag(UpdateTvShowWatchingFlagParams params) { repository.updateTvShowWatchingFlag(params);}

    public void deleteTvShow(int id) { repository.deleteTvShow(id);}

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

    private Boolean isTvShowExist(int tvShowId) {
        boolean isExist = false;
        for (int i = 0; i < allTvShows.getValue().size(); i++) {
            TvShow tvShow = allTvShows.getValue().get(i);
            if (tvShowId == tvShow.getTvShowId()) {
                isExist = true;
                return isExist;
            }
        }
        return isExist;
    }
    private Boolean isFlagYes(String flag){
        if(flag.equals("yes")){
            return true;
        }
        return  false;
    }
    private void insertTvShows(JSON_RootElement data){
        for(int i=0; i < data.getTVShows().size();i++){
            TV_Show urlTvShow = data.getTVShows().get(i);

            int tvShowId = urlTvShow.getId();
            String tvShowName = urlTvShow.getName();
            String tvShowStatus = urlTvShow.getStatus();

            TvShow tvShow = new TvShow(tvShowName, tvShowStatus, tvShowId);
        if(isTvShowExist(tvShowId)){
            repository.updateTvShow(tvShow);
        }else {
            repository.insertTvShow(tvShow);
        }
        }
    }

    private void deleteNotInServer(JSON_RootElement data) {
        boolean isForDeleting;
        for (int i = 0; i < allTvShows.getValue().size(); i++) {
            isForDeleting = true;
            for (int j = 0; j < data.getTVShows().size(); j++) {
                String tvShowDB = String.valueOf(allTvShows.getValue().get(i).getTvShowId());
                String tvShowServer = String.valueOf(data.getTVShows().get(j).getId());

                if (tvShowDB.compareTo(tvShowServer) == 0) {
                    isForDeleting = false;
                    break;
                }
            }
            if (isForDeleting) {
                repository.deleteTvShow(allTvShows.getValue().get(i).getTvShowId());
            }
        }
    }

    private void InsertTvShows(){
        JSON_RootElement data = downloadDataFromURL();
        deleteNotInServer(data);
        insertTvShows(data);
    }

    public void syncTvShows(){
        InsertTvShows();
    }


}
