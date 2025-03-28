package com.example.weatherapp.data.repository

import com.example.weatherapp.data.models.ForecastResponseApi
import com.example.weatherapp.data.models.CityLocation
import com.example.weatherapp.data.models.CityResponse
import com.example.weatherapp.data.models.CurrentResponseApi
import kotlinx.coroutines.flow.Flow

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


    suspend fun getCityByLatLon(
        lat: Double,
        lon: Double
    ): List<CityResponse.CityResponseItem>

    suspend fun getFavouriteCityLocations(): Flow<List<CityLocation>>


    suspend fun insertCityLocation(cityLocation: CityLocation): Long


    suspend fun deleteCityLocation(cityLocation: CityLocation): Int
}