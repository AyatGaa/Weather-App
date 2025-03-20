package com.example.weatherapp.data.remote

import ForecastResponseApi
import com.example.weatherapp.data.models.CurrentResponseApi
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface WeatherRemoteDataSource {

    suspend fun currentWeather(lat:Double, lon:Double, lang:String): Flow<CurrentResponseApi>
    suspend fun forecastWeather(lat:Double, lon:Double, lang:String): Flow<ForecastResponseApi>
}