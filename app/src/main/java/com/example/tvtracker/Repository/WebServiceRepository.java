package com.example.tvtracker.Repository;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.tvtracker.Api.ApiBuilder;
import com.example.tvtracker.Api.ApiService;
import com.example.tvtracker.JsonModels.TvShowBasicInfo.JsonTvShowBasicRoot;
import com.example.tvtracker.Models.TvShow;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WebServiceRepository {
    Application application;
    public WebServiceRepository(Application application){
        this.application = application;
    }


    List<TvShow> webserviceResponseList = new ArrayList<>();

    public LiveData<List<TvShow>> providesWebService() {

        final MutableLiveData<List<TvShow>> data = new MutableLiveData<>();
        String response = "";
        try {
            ApiService apiService = ApiBuilder.getRetrofitInstance().create(ApiService.class);

//            Call<JsonTvShowBasicRoot> jsonTvShowBasicRootCall = apiService.getTvShowsBasic(pageNum);
                apiService.getTvShowsBasic(1).enqueue(new Callback<JsonTvShowBasicRoot>() {
                    @Override
                    public void onResponse(Call<JsonTvShowBasicRoot> call, Response<JsonTvShowBasicRoot> response) {
                        Log.d("Repository", "Success");
                        webserviceResponseList = response.body().toTvShowArray();
                        AppRepository appRepository = new AppRepository(application);
                        appRepository.insertTvShows(webserviceResponseList);
                        data.setValue(webserviceResponseList);

                    }

                    @Override
                    public void onFailure(Call<JsonTvShowBasicRoot> call, Throwable t) {
                        Log.d("Repository", "Failed");
                    }
                });
//            jsonTvShowBasicRootCall.enqueue
        }catch (Exception e) {
        e.printStackTrace();
        }

        return data;


    }


}
