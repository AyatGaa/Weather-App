package com.example.weatherapp.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.weatherapp.data.local.Converters

@Entity(tableName = "home")
@TypeConverters(Converters::class)
data class HomeEntity(
    @PrimaryKey
    val id: Int = 0,
    val currentWeather: CurrentResponseApi,
    val hourlyWeather: List<ForecastItem>,
    val dailyWeather: List<ForecastItem>
)
