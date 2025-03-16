package com.example.weatherapp.data.repository

import com.example.weatherapp.data.models.CurrentResponseApi
import com.example.weatherapp.data.remote.WeatherRemoteDataSource
import retrofit2.Response

class WeatherRepositoryImpl(
    private val remoteDataSource: WeatherRemoteDataSource,
)  : WeatherRepository {


    companion object {
        private var INSTANCE: WeatherRepositoryImpl? = null
        fun getInstance(
            remoteDataSource: WeatherRemoteDataSource,
         ): WeatherRepository {
            return INSTANCE ?: synchronized(this) {
                val inst = WeatherRepositoryImpl(remoteDataSource)
                INSTANCE = inst
                inst
            }
        }
    }

    override suspend fun getCurrentWeather(lat: Double, lon: Double, units: String, lang: String) :Response<CurrentResponseApi>{
       return remoteDataSource.currentWeather(lat,lon,units,lang)
    }

}