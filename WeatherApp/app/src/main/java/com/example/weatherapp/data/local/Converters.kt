package com.example.weatherapp.data.local

import com.example.weatherapp.data.models.ForecastResponseApi
import androidx.room.TypeConverter
import com.example.weatherapp.data.models.CityResponse.CityResponseItem
import com.example.weatherapp.data.models.CurrentResponseApi
 import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {

    private val gson = Gson()

    @TypeConverter
    fun fromCityResponseItem(city: CityResponseItem?): String {
        return gson.toJson(city)
    }

    @TypeConverter
    fun toCityResponseItem(json: String): CityResponseItem {
        val type = object : TypeToken<CityResponseItem>() {}.type
        return gson.fromJson(json, type)
    }

    @TypeConverter
    fun fromCurrentWeather(currentWeather: CurrentResponseApi?): String {
        return gson.toJson(currentWeather)
    }

    @TypeConverter
    fun toCurrentWeather(json: String): CurrentResponseApi {
        val type = object : TypeToken<CurrentResponseApi>() {}.type
        return gson.fromJson(json, type)
    }

    @TypeConverter
    fun fromForecastWeather(forecastWeather: ForecastResponseApi?): String {
        return gson.toJson(forecastWeather)
    }

    @TypeConverter
    fun toForecastWeather(json: String): ForecastResponseApi {
        val type = object : TypeToken<ForecastResponseApi>() {}.type
        return gson.fromJson(json, type)
    }
}
