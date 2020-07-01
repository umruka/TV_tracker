package com.example.tvtracker.UI.Statistics;

import android.app.Application;
import android.util.Pair;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.example.tvtracker.Models.Profile;
import com.example.tvtracker.Models.TvShowFull;
import com.example.tvtracker.Repository.AppRepository;

import java.util.List;

public class StatisticsViewModel extends AndroidViewModel {
    private AppRepository repository;
    private MediatorLiveData<List<String>> statisticsListObservable = new MediatorLiveData<>();

    public StatisticsViewModel(@NonNull Application application) {
        super(application);
        repository = new AppRepository(application);
        statisticsListObservable.addSource(repository.getStatisticsTvShowsListObservable(), new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> stringList) {
                statisticsListObservable.setValue(stringList);
            }
        });

    }
    public LiveData<List<String>> getStatisticsListObservable() {
        return statisticsListObservable;
    }

    void fetchStatisticsData() {
        repository.fetchStatistics();
    }
}
