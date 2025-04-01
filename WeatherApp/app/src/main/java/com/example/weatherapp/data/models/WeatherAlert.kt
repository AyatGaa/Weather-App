package com.example.weatherapp.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "alert_table")
data class WeatherAlert(
    @PrimaryKey
    val id: Int = 0,
    var cityName: String,
    val startDate: Long,
    val endDate: Long,
    val lat: Double,
    val lon: Double,
    )