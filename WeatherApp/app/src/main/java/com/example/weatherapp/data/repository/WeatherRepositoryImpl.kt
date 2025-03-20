package com.example.weatherapp.data.repository

import ForecastResponseApi
import com.example.weatherapp.data.models.CurrentResponseApi
import com.example.weatherapp.data.remote.WeatherRemoteDataSource
import kotlinx.coroutines.flow.Flow
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

    override suspend fun getCurrentWeather(lat: Double, lon: Double, lang: String) :Flow<CurrentResponseApi>{
       return remoteDataSource.currentWeather(lat,lon,lang)
    }

    override suspend fun getForecastWeather(
        lat: Double,
        lon: Double,
        lang: String
    ): Flow<ForecastResponseApi> {
        return remoteDataSource.forecastWeather(lat,lon,lang)
    }

}