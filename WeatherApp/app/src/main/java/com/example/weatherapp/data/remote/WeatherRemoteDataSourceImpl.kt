package com.example.weatherapp.data.remote

import com.example.weatherapp.BuildConfig
import com.example.weatherapp.data.models.CurrentResponseApi
import retrofit2.Response

class WeatherRemoteDataSourceImpl(private val apiService: WeatherApiService) :
    WeatherRemoteDataSource {
    override suspend fun currentWeather(
        lat: Double,
        lon: Double,
        units: String,
        lang: String

    ): Response<CurrentResponseApi> {
        return apiService.getCurrentWeather(lat, lon, units, lang, BuildConfig.apiKeySafe)
    }

}