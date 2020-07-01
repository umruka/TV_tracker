package com.example.tvtracker.UI.Calendar;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;

import com.example.tvtracker.Models.CalendarTvShowEpisode;
import com.example.tvtracker.Repository.AppRepository;

import java.util.List;

public class CalendarViewModel extends AndroidViewModel {
    private AppRepository repository;
    private MediatorLiveData<List<CalendarTvShowEpisode>> calendarListObservable = new MediatorLiveData<>();

    public CalendarViewModel(@NonNull Application application) {
        super(application);
        repository = new AppRepository(application);
        calendarListObservable.addSource(repository.getCalendarListObservable(), new Observer<List<CalendarTvShowEpisode>>() {
            @Override
            public void onChanged(List<CalendarTvShowEpisode> tvShowFulls) {
                calendarListObservable.setValue(tvShowFulls);
            }
        });
    }

    LiveData<List<CalendarTvShowEpisode>> getCalendarListObservable() {
        return calendarListObservable;
    }

    void fetchCalendarData() {
        repository.fetchCalendar();
    }


}
