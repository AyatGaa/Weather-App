package com.example.weatherapp.data.models

import com.google.gson.annotations.SerializedName

data class ForecastResponseApi(
    @SerializedName("list") val list: List<ForecastItem>
)

data class ForecastItem(
    @SerializedName("dt") val timestamp: Long,
    @SerializedName("main") val main: Main,
    @SerializedName("weather") val weather: List<Weather>,
    @SerializedName("wind") val wind: Wind
)
