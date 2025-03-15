package com.example.weatherapp.remote

import com.example.weatherapp.models.CurrentResponseApi
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {

        @GET("data/2.5/weather")
       suspend fun getCurrentWeather(
            @Query("lat") lat :Double,
            @Query("lon") lon:Double,
            @Query("units") units :String,
            @Query("lang") language:String,
            @Query("appid") apiKey:String
        ): Response<CurrentResponseApi>

      /* @GET("data/2.5/forecast")
       suspend fun getForecastWeather(
            @Query("lat") lat :Double,
            @Query("lon") lon:Double,
            @Query("units") units :String,
            @Query("lang") language:String,
            @Query("appid") apiKey:String
        ): Response<CurrentResponseApi>*/



}