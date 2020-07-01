package com.example.tvtracker.UI;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;

import com.example.tvtracker.Repository.AppRepository;

public class SyncViewModel extends AndroidViewModel {
    private AppRepository repository;
    private MediatorLiveData<Boolean> syncStateObservable = new MediatorLiveData<>();

    public SyncViewModel(@NonNull Application application) {
        super(application);
        repository = new AppRepository(application);

        if(!MainActivity.TEST_MODE){
            repository.initialFetchData();
        }else {
            repository.initialFetchTestData();
        }
        syncStateObservable.addSource(repository.getSyncState(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                syncStateObservable.setValue(aBoolean);
            }
        });
    }

    MediatorLiveData<Boolean> getSyncStateObservable() {
        return syncStateObservable;
    }
}
