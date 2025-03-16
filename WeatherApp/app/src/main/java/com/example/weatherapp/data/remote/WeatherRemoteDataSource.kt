package com.example.weatherapp.data.remote

import com.example.weatherapp.data.models.CurrentResponseApi
import retrofit2.Response

interface WeatherRemoteDataSource {

    suspend fun currentWeather(lat:Double, lon:Double, units:String, lang:String):Response<CurrentResponseApi>
}