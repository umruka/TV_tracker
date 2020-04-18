package com.example.tvtracker.Api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Headers;

public class ApiBuilder {

    private static Retrofit retrofit;
    private static final String BASE_URL = "https://www.episodate.com/api/";
    public static Retrofit getRetrofitInstance() {
        if(retrofit == null) {


            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;

    }

}
