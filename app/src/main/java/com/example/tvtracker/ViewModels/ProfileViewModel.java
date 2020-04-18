package com.example.tvtracker.ViewModels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.tvtracker.Models.Profile;

import java.util.List;

public class ProfileViewModel extends ViewModel {
    private MutableLiveData<List<Profile>> profiles;
    public MutableLiveData<List<Profile>> getProfiles() {
        if (profiles == null) {
            profiles = new MutableLiveData<List<Profile>>();
            loadProfiles();
        }
        return profiles;
    }

    private void loadProfiles() {
        Profile profile = new Profile("Asen",18,"Male");
        //profiles.
    }
    // TODO: Implement the ViewModel
}
