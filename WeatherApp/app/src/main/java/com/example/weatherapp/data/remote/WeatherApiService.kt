package com.example.weatherapp.data.remote

import com.example.weatherapp.data.models.ForecastResponseApi
import com.example.weatherapp.BuildConfig
import com.example.weatherapp.data.models.CityResponse
import com.example.weatherapp.data.models.CurrentResponseApi
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {
    // we don't add Flow here as, it the API request, as retrofit is One-Time Request opposite of FLow
    // we can wrap the Response in Flow
    @GET("data/2.5/weather")
    suspend fun getCurrentWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("lang") language: String,
        @Query("units") units: String,
        @Query("appid") apiKey: String = BuildConfig.apiKeySafe
    ): Response<CurrentResponseApi>

    @GET("data/2.5/forecast")
    suspend fun getForecastWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("lang") language: String,
        @Query("units") units: String,
        @Query("appid") apiKey: String = BuildConfig.apiKeySafe
    ): Response<ForecastResponseApi>


    @GET("geo/1.0/reverse")
    suspend fun getLocationDetails(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("limit") limit: Int = 1,
        @Query("appid") apiKey: String = BuildConfig.apiKeySafe
    ):List<CityResponse.CityResponseItem>


}