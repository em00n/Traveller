package com.emon.traveller;

import com.emon.traveller.model.WeatherModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface OpenWeatherMap {


    @GET("data/2.5/weather?")
    Call<WeatherModel> getCurrentWeather(
            @Query("lat") String lat,
            @Query("lon") String lon,
            @Query("APPID") String appid
    );

    @GET
    Call<WeatherModel> getCurrentWeather(@Url String url);
}
