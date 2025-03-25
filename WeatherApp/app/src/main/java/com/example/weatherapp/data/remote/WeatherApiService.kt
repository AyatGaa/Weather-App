package com.example.weatherapp.data.remote

import ForecastResponseApi
import com.example.weatherapp.BuildConfig
import com.example.weatherapp.data.models.CurrentResponseApi
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {

        @GET("data/2.5/weather")
       suspend fun getCurrentWeather(
            @Query("lat") lat :Double,
            @Query("lon") lon:Double,
            @Query("lang") language:String,
            @Query("units") units:String,
            @Query("appid") apiKey:String  = BuildConfig.apiKeySafe
        ): Response<CurrentResponseApi>

      @GET("data/2.5/forecast")
       suspend fun getForecastWeather(
            @Query("lat") lat :Double,
            @Query("lon") lon:Double,
            @Query("lang") language:String,
            @Query("units") units:String,
            @Query("appid") apiKey:String = BuildConfig.apiKeySafe
        ): Response<ForecastResponseApi>



}