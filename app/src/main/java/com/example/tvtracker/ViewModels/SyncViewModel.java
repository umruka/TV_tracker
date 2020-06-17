package com.example.tvtracker.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;

import com.example.tvtracker.Repository.AppRepository;

public class SyncViewModel extends AndroidViewModel {
    private AppRepository repository;

    private MediatorLiveData<Boolean> syncStateObservable = new MediatorLiveData<>();

    public SyncViewModel(@NonNull Application application) {
        super(application);
        repository = new AppRepository(application);
        repository.fetchAllData();
        syncStateObservable.addSource(repository.getSyncState(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                syncStateObservable.setValue(aBoolean);
            }
        });
    }

    public MediatorLiveData<Boolean> getSyncStateObservable() {
        return syncStateObservable;
    }
}