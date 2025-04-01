package com.example.weatherapp.data.repository

import com.example.weatherapp.data.models.ForecastResponseApi
import com.example.weatherapp.data.models.CityLocation
import com.example.weatherapp.data.models.CityResponse
import com.example.weatherapp.data.models.CurrentResponseApi
import com.example.weatherapp.data.models.WeatherAlert
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {
    //remote
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


    //local
    //favorite
    suspend fun getFavouriteCityLocations(): Flow<List<CityLocation>>

    suspend fun getCityById(  cityId:Int ) : CityLocation

    suspend fun insertCityLocation(cityLocation: CityLocation): Long

    suspend fun deleteCityLocation(cityLocation: CityLocation): Int

    //alert
    suspend fun insertAlert(alert: WeatherAlert) : Long

    suspend fun deleteAlert(alert: WeatherAlert):Int

    suspend fun getAllWeatherAlert():Flow<List<WeatherAlert>>

    suspend fun deleteAlertById(alertId: Int): Int
    suspend fun getAlertByTime(start: Long, end: Long): WeatherAlert?

    suspend fun getAlertByID( id:Int):WeatherAlert
}