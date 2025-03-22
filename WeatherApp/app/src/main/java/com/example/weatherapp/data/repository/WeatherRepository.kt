package com.example.weatherapp.data.repository

import ForecastResponseApi
import android.location.Location
import com.example.weatherapp.data.models.CurrentResponseApi
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface WeatherRepository {

    suspend fun getCurrentWeather(lat:Double, lon:Double, lang:String): Flow<CurrentResponseApi>
    suspend fun getForecastWeather(lat:Double, lon:Double, lang:String): Flow<ForecastResponseApi>


}