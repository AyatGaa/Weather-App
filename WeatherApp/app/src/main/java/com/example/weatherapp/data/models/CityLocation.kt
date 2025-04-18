package com.example.weatherapp.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.weatherapp.data.local.Converters
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "city_location" )
@TypeConverters(Converters::class)
data class CityLocation(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    val cityData: CityResponse.CityResponseItem,
    val lat: Double,
    val lon: Double,
    val currentWeather: CurrentResponseApi,
    val forecastWeather: ForecastResponseApi,
    val flag: String

)
