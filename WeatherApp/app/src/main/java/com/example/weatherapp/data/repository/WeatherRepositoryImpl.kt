package com.example.weatherapp.data.repository

import com.example.weatherapp.data.models.ForecastResponseApi
import com.example.weatherapp.data.local.CityLocationLocalDataSource
import com.example.weatherapp.data.models.CityLocation
import com.example.weatherapp.data.models.CityResponse
import com.example.weatherapp.data.models.CurrentResponseApi
import com.example.weatherapp.data.models.WeatherAlert
import com.example.weatherapp.data.remote.WeatherRemoteDataSource
import kotlinx.coroutines.flow.Flow

class WeatherRepositoryImpl(
    private val remoteDataSource: WeatherRemoteDataSource,
    private val localDataSource: CityLocationLocalDataSource
) : WeatherRepository {


    companion object {
        private var INSTANCE: WeatherRepositoryImpl? = null
        fun getInstance(
            remoteDataSource: WeatherRemoteDataSource,
            localDataSource: CityLocationLocalDataSource
        ): WeatherRepository {
            return INSTANCE ?: synchronized(this) {
                val inst = WeatherRepositoryImpl(remoteDataSource, localDataSource)
                INSTANCE = inst
                inst
            }
        }
    }

    override suspend fun getCurrentWeather(
        lat: Double,
        lon: Double,
        lang: String,
        units: String
    ): Flow<CurrentResponseApi> {
        return remoteDataSource.currentWeather(lat, lon, lang, units)
    }

    override suspend fun getForecastWeather(
        lat: Double,
        lon: Double,
        lang: String,
        units: String
    ): Flow<ForecastResponseApi> {
        return remoteDataSource.forecastWeather(lat, lon, lang, units)
    }

    override suspend fun getCityByLatLon(
        lat: Double,
        lon: Double
    ): List<CityResponse.CityResponseItem> {
        return remoteDataSource.getCityByLatLon(lat, lon)
    }

    override suspend fun getFavouriteCityLocations(): Flow<List<CityLocation>> {
        return localDataSource.getFavouriteCityLocations()
    }

    override suspend fun getCityById(cityId: Int): CityLocation {
        return localDataSource.getCityById(cityId)
    }

    override suspend fun insertCityLocation(cityLocation: CityLocation): Long {
        return localDataSource.insertCityLocation(cityLocation)
    }

    override suspend fun deleteCityLocation(cityLocation: CityLocation): Int {
        return localDataSource.deleteCityLocation(cityLocation)
    }

    override suspend fun insertAlert(alert: WeatherAlert): Long {
        return localDataSource.insertAlert(alert)
    }

    override suspend fun deleteAlert(alert: WeatherAlert): Int {
        return localDataSource.deleteAlert(alert)
    }

    override suspend fun getAllWeatherAlert(): Flow<List<WeatherAlert>> {
        return localDataSource.getAllWeatherAlert()
    }

    override suspend fun deleteAlertById(alertId: Int): Int {
        return localDataSource.deleteAlertById(alertId)
    }

    override suspend fun getAlertByTime(start: Long, end: Long): WeatherAlert? {
        return localDataSource.getAlertByTime(start,end)
    }

    override suspend fun getAlertByID( id: Int): WeatherAlert {
        return localDataSource.getAlertByID( id)
    }

}