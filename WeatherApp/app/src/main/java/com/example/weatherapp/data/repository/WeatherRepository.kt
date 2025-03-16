package com.example.weatherapp.data.repository

import com.example.weatherapp.data.models.CurrentResponseApi
import retrofit2.Response

interface WeatherRepository {

    suspend fun getCurrentWeather(lat:Double, lon:Double, units:String, lang:String):Response<CurrentResponseApi>

}