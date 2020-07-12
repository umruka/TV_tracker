package com.watermelon.UI.Calendar;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import com.watermelon.Models.TvSeriesCalendarEpisode;
import com.watermelon.Repository.AppRepository;

import java.util.List;

public class CalendarViewModel extends AndroidViewModel {
    private AppRepository repository;
    private LiveData<List<TvSeriesCalendarEpisode>> calendarListObservable;

    public CalendarViewModel(@NonNull Application application) {
        super(application);
        repository = new AppRepository(application);
        calendarListObservable = repository.getCalendarListObservable();
    }

    LiveData<List<TvSeriesCalendarEpisode>> getCalendarListObservable() {
        return calendarListObservable;
    }


}
