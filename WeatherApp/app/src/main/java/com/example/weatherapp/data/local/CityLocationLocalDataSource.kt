package com.example.weatherapp.data.local

import com.example.weatherapp.data.models.CityLocation
import com.example.weatherapp.data.models.HomeEntity
import com.example.weatherapp.data.models.WeatherAlert
import kotlinx.coroutines.flow.Flow

interface CityLocationLocalDataSource {
    //favorite
    suspend fun getFavouriteCityLocations(): Flow<List<CityLocation>>
    suspend fun getCityById(  cityId:Int) : CityLocation
    suspend fun insertCityLocation(cityLocation: CityLocation): Long
    suspend fun deleteCityLocation(cityLocation: CityLocation): Int

    //alert
    suspend fun insertAlert(alert: WeatherAlert) : Long

    suspend fun deleteAlert(alert: WeatherAlert):Int

    suspend fun getAllWeatherAlert():Flow<List<WeatherAlert>>

    suspend fun getAlertByID( id:Int):WeatherAlert

    suspend fun deleteAlertById(alertId: Int): Int

     suspend fun getAlertByTime(start: Long, end: Long): WeatherAlert?


     //home cache
     suspend fun getHomeData(): HomeEntity

     suspend fun insertHomeData(homeEntity: HomeEntity): Long
}