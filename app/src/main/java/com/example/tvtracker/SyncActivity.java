package com.example.tvtracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.widget.RelativeLayout;

import com.example.tvtracker.ViewModels.SyncViewModel;

public class SyncActivity extends AppCompatActivity   {

private SyncViewModel syncViewModel;
    private RelativeLayout syncView;
public static final int numOfTasks = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_sync);

        syncView = findViewById(R.id.sync_view_sync_act);
        syncViewModel = new ViewModelProvider(this).get(SyncViewModel.class);

        syncViewModel.getSyncStateObservable().observe( this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean){
                    Intent intent = new Intent(SyncActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        });




    }
}
