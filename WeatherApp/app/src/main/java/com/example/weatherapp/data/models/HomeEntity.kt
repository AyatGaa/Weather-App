package com.example.weatherapp.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "home")
data class HomeEntity(
    @PrimaryKey
    val id:Int = 0,
    val currentResponseApi :CurrentResponseApi,
    val forecastItem: ForecastResponseApi
)
/**
 * Dao
    insert home
    get home

3 tables

 */
