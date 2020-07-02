package com.watermelon.UI.Calendar;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;

import com.watermelon.Models.CalendarTvShowEpisode;
import com.watermelon.Repository.AppRepository;

import java.util.List;

public class CalendarViewModel extends AndroidViewModel {
    private AppRepository repository;
    private LiveData<List<CalendarTvShowEpisode>> calendarListObservable = new MediatorLiveData<>();

    public CalendarViewModel(@NonNull Application application) {
        super(application);
        repository = new AppRepository(application);
        calendarListObservable = repository.getCalendarListObservable();
    }

    LiveData<List<CalendarTvShowEpisode>> getCalendarListObservable() {
        return calendarListObservable;
    }


}
