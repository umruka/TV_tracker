package com.example.tvtracker.ViewModels;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.tvtracker.JsonModels.TvShowBasic.JsonTvShowBasicRoot;
import com.example.tvtracker.JsonModels.TvShowBasic.JsonTvShowBasic;
import com.example.tvtracker.Models.TvShowBasic;
import com.example.tvtracker.Repository.AppRepository;
import com.example.tvtracker.Models.UpdateTvShowWatchingFlagParams;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class TvShowBasicViewModel extends AndroidViewModel {
    private AppRepository repository;
    private LiveData<List<TvShowBasic>> allTvShows;

    public TvShowBasicViewModel(@NonNull Application application) {
        super(application);
        repository = new AppRepository(application);
        allTvShows = repository.getAllTvShows();
    }

    public LiveData<List<TvShowBasic>> getAllTvShows() {
        return allTvShows;
    }

    public void insert(TvShowBasic tvShowBasic) {
        repository.insertTvShow(tvShowBasic);
    }

    public void deleteAllTvShows() {
        repository.deleteAllTvShows();
    }

    public void updateTvShow(TvShowBasic tvShowBasic) {
        repository.updateTvShow(tvShowBasic);
    }

    public void updateTvShowWatchingFlag(UpdateTvShowWatchingFlagParams params) {
        repository.updateTvShowWatchingFlag(params);
    }

    public void deleteTvShow(int id) {
        repository.deleteTvShow(id);
    }

    public TvShowBasic getTvShow(int id) {
        return repository.getTvShowById(id);
    }

    private JsonTvShowBasicRoot downloadDataFromURL(int page) {
        Context context = getApplication().getApplicationContext();

        String webPage = "https://www.episodate.com/api/most-popular?page=" + page;
        JsonTvShowBasicRoot jsonRootElement = new JsonTvShowBasicRoot();
        try (InputStream is = new URL(webPage).openStream(); Reader reader = new InputStreamReader(is, StandardCharsets.UTF_8)) {
            Gson gson = new Gson();
            jsonRootElement = gson.fromJson(reader, JsonTvShowBasicRoot.class);
            return jsonRootElement;
        } catch (IOException e) {
            e.getMessage();
            return jsonRootElement;
        }
    }

    private Boolean isTvShowExist(int tvShowId) {
        boolean isExist = false;
        for (int i = 0; i < allTvShows.getValue().size(); i++) {
            TvShowBasic tvShowBasic = allTvShows.getValue().get(i);
            if (tvShowId == tvShowBasic.getTvShowId()) {
                isExist = true;
                return isExist;
            }
        }
        return isExist;
    }

    private void insertTvShows(JsonTvShowBasicRoot data) {
        for (int i = 0; i < data.getTVShows().size(); i++) {
            JsonTvShowBasic urlTvShow = data.getTVShows().get(i);

            int tvShowId = urlTvShow.getId();
            String tvShowName = urlTvShow.getName();
            String tvShowStatus = urlTvShow.getStatus();
            String tvShowStartDate = urlTvShow.getStartDate();
            String tvShowEndDate = urlTvShow.getEndDate();
            String tvShowCountry = urlTvShow.getCountry();
            String tvShowNetwork = urlTvShow.getNetwork();
            String tvShowImage = urlTvShow.getImageThumbnailPath();


            TvShowBasic tvShowBasic = new TvShowBasic(tvShowId, tvShowName, tvShowStartDate, tvShowEndDate, tvShowCountry, tvShowNetwork, tvShowStatus, tvShowImage);
            if (isTvShowExist(tvShowId)) {
                repository.updateTvShow(tvShowBasic);
            } else {
                repository.insertTvShow(tvShowBasic);
            }
        }
    }

    private void deleteNotInServer(JsonTvShowBasicRoot data) {
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

    private void InsertTvShows() {
        for (int i = 1; i <= 5; i++) {
            JsonTvShowBasicRoot data = downloadDataFromURL(i);
            insertTvShows(data);

        }
        //deleteNotInServer(data);
    }

    public void syncTvShows() {
        //InsertTvShows();
        repository.getMostPopularTvShowsBasicInfo();
    }


}
