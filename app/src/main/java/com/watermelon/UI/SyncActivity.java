package com.watermelon.UI;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.watermelon.Helpers.ConnectivityHelper;
import com.watermelon.R;

public class SyncActivity extends AppCompatActivity implements DialogInterface.OnClickListener   {

private SyncViewModel syncViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_sync);

        if (ConnectivityHelper.isConnectedFast(this)) {
            syncViewModel = new ViewModelProvider(this).get(SyncViewModel.class);
            syncViewModel.getSyncStateObservable().observe( this, new Observer<Boolean>() {
                @Override
                public void onChanged(Boolean aBoolean) {
                    if(aBoolean){
                        Intent intent = new Intent(SyncActivity.this, WatermelonMainActivity.class);
                        startActivity(intent);
                    }
                }
            });
        }else {
            try {
                new AlertDialog.Builder(this)
                        .setTitle("Info")
                        .setMessage("Internet not available, Check your internet connectivity and try again")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton("OK", this)
                        .show();
        } catch (Exception e) {
            Log.d("Problem", "Show Dialog: " + e.getMessage());
        }
        }



    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
        finish();
    }
}
