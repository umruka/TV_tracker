package com.example.tvtracker.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;

import com.example.tvtracker.Models.TvShow;
import com.example.tvtracker.Models.TvShowEpisode;
import com.example.tvtracker.Repository.AppRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class CalendarViewModel extends AndroidViewModel {
    private AppRepository repository;
    private MediatorLiveData<List<TvShowEpisode>> calendarListObservable = new MediatorLiveData<>();
    public CalendarViewModel(@NonNull Application application){
        super(application);
        repository = new AppRepository(application);
        calendarListObservable.addSource(repository.getLast30daysEpisodes(), new Observer<List<TvShowEpisode>>() {
            @Override
            public void onChanged(List<TvShowEpisode> tvShowEpisodes) {
                calendarListObservable.setValue(tvShowEpisodes);
            }
        });
    }

    public LiveData<List<TvShowEpisode>> getCalendarListObservable() {
        return calendarListObservable;
    }

    public List<String> getUniqueDates(){
        List<TvShowEpisode> episodes = new ArrayList<>();
        if(calendarListObservable.getValue() != null){
            episodes = calendarListObservable.getValue();
        }
        List<String> dates = new ArrayList<>();
        for (TvShowEpisode episode : episodes){
            dates.add(episode.getEpisodeAirDate());
        }
        List<String> uniqueDates = new ArrayList<>(new HashSet<String>(dates));
        return uniqueDates;
    }

    public void fetchData() {
        repository.fetchEpisodesForCalendar();
    }


}
