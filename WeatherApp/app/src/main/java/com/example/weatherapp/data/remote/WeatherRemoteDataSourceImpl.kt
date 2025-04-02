package com.example.weatherapp.data.remote

import android.util.Log
import com.example.weatherapp.data.models.CityResponse
import com.example.weatherapp.data.models.CurrentResponseApi
import com.example.weatherapp.data.models.ForecastResponseApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.math.log

class WeatherRemoteDataSourceImpl(private val apiService: WeatherApiService) :
    WeatherRemoteDataSource {


    override suspend fun currentWeather(
        lat: Double,
        lon: Double,
        lang: String,
        units: String

    ): Flow<CurrentResponseApi> {
        return flow {
            try {

                val latestWeather =
                    apiService.getCurrentWeather(lat, lon, lang, units)

                if (latestWeather.isSuccessful) {
                    val response = latestWeather.body()
                    if (response != null) {
                        emit(response)
                    } else {
                        
                    }

                } else {

                }

            } catch (e: Exception) {
                Log.d("TAG", "currentWeather: IN Remote Weather Source ${e.message}")

            }

        }
    }

    override suspend fun forecastWeather(
        lat: Double,
        lon: Double,
        lang: String,
        units: String
    ): Flow<ForecastResponseApi> {
        return flow {
            try {
                val latestWeather =
                    apiService.getForecastWeather(lat, lon, lang, units)

                if (latestWeather.isSuccessful) {
                    val response = latestWeather.body()
                    if (response != null) {
                        emit(response)
                    } else {
                        Log.w("TAG", "forecastWeather: Response is NULL")
                    }
                } else {
                    Log.w("TAG", "forecastWeather: Response Failed")
                }

            } catch (e: Exception) {
                Log.w("TAG", "forecastWeather: Can not fetch data from API")
            }

        }
    }

    override suspend fun getCityByLatLon(
        lat: Double,
        lon: Double
    ): List<CityResponse.CityResponseItem> {
        return apiService.getLocationDetails(lat, lon)
    }
}

