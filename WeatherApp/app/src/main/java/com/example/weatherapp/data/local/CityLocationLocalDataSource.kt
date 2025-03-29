package com.example.weatherapp.data.local

import com.example.weatherapp.data.models.CityLocation
import kotlinx.coroutines.flow.Flow

interface CityLocationLocalDataSource {


    suspend fun getFavouriteCityLocations(): Flow<List<CityLocation>>

    suspend fun getCityById(  cityId:Int) : CityLocation

    suspend fun insertCityLocation(cityLocation: CityLocation): Long


    suspend fun deleteCityLocation(cityLocation: CityLocation): Int
}