package com.example.weatherapp.data.remote

import com.example.weatherapp.data.models.ForecastResponseApi
import com.example.weatherapp.data.models.CityResponse
import com.example.weatherapp.data.models.CurrentResponseApi
import kotlinx.coroutines.flow.Flow

interface WeatherRemoteDataSource {

    suspend fun currentWeather(lat:Double, lon:Double, lang:String, units:String): Flow<CurrentResponseApi>
    suspend fun forecastWeather(lat:Double, lon:Double, lang:String, units:String): Flow<ForecastResponseApi>
    suspend fun getCityByLatLon(lat:Double, lon:Double): List<CityResponse.CityResponseItem>
}