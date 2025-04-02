package com.example.weatherapp.data.local

import com.example.weatherapp.data.models.CityLocation
import com.example.weatherapp.data.models.CityResponse
import com.example.weatherapp.data.models.CurrentResponseApi
import com.example.weatherapp.data.models.ForecastResponseApi
import com.example.weatherapp.data.remote.WeatherRemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeRemoteDataSource( var currentResponse:CurrentResponseApi  ): WeatherRemoteDataSource{
    override suspend fun currentWeather(
        lat: Double,
        lon: Double,
        lang: String,
        units: String
    ): Flow<CurrentResponseApi> {
        return flow {emit(currentResponse)}
    }

    override suspend fun forecastWeather(
        lat: Double,
        lon: Double,
        lang: String,
        units: String
    ): Flow<ForecastResponseApi> {
        TODO("Not yet implemented")
    }

    override suspend fun getCityByLatLon(
        lat: Double,
        lon: Double
    ): List<CityResponse.CityResponseItem> {
        TODO("Not yet implemented")
    }
}