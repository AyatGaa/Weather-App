package com.example.weatherapp.data.repository

import ForecastResponseApi
import android.location.Location
import com.example.weatherapp.data.local.CityLocationLocalDataSource
import com.example.weatherapp.data.models.CityLocation
import com.example.weatherapp.data.models.CurrentResponseApi
import com.example.weatherapp.data.remote.WeatherRemoteDataSource
import com.example.weatherapp.utils.location.LocationClient
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

class WeatherRepositoryImpl(
    private val remoteDataSource: WeatherRemoteDataSource,
    private val localDataSource: CityLocationLocalDataSource
)  : WeatherRepository {


    companion object {
        private var INSTANCE: WeatherRepositoryImpl? = null
        fun getInstance(
            remoteDataSource: WeatherRemoteDataSource,
            localDataSource: CityLocationLocalDataSource
         ): WeatherRepository {
            return INSTANCE ?: synchronized(this) {
                val inst = WeatherRepositoryImpl(remoteDataSource,localDataSource)
                INSTANCE = inst
                inst
            }
        }
    }

    override suspend fun getCurrentWeather(lat: Double, lon: Double, lang: String, units:String) :Flow<CurrentResponseApi>{
       return remoteDataSource.currentWeather(lat,lon,lang,units)
    }

    override suspend fun getForecastWeather(
        lat: Double,
        lon: Double,
        lang: String,
        units:String
    ): Flow<ForecastResponseApi> {
        return remoteDataSource.forecastWeather(lat,lon,lang,units)
    }

    override suspend fun getFavouriteCityLocations(): Flow<List<CityLocation>> {
        return  localDataSource.getFavouriteCityLocations()
    }

    override suspend fun insertCityLocation(cityLocation: CityLocation): Long {
        return localDataSource.insertCityLocation(cityLocation)
    }

    override suspend fun deleteCityLocation(cityLocation: CityLocation): Int {
        return localDataSource.deleteCityLocation(cityLocation)
    }

}