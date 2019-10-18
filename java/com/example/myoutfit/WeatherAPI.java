package com.example.myoutfit;

import com.example.myoutfit.data.datamodel.WeatherModel;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface WeatherAPI {

    @GET("current.json")
    @Headers("Accept:application/json")
    Call<WeatherModel> getWeather(@Query("key") String key, @Query("q") String location);
}

