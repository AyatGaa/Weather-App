package com.example.weatherapp.data.repository

import ForecastResponseApi
import android.location.Location
import com.example.weatherapp.data.models.CityLocation
import com.example.weatherapp.data.models.CurrentResponseApi
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface WeatherRepository {

    suspend fun getCurrentWeather(
        lat: Double,
        lon: Double,
        lang: String,
        units: String
    ): Flow<CurrentResponseApi>

    suspend fun getForecastWeather(
        lat: Double,
        lon: Double,
        lang: String,
        units: String
    ): Flow<ForecastResponseApi>


    suspend fun getFavouriteCityLocations(): Flow<List<CityLocation>>


    suspend fun insertCityLocation(cityLocation: CityLocation): Long


    suspend fun deleteCityLocation(cityLocation: CityLocation): Int
}