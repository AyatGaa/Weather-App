package com.example.weatherapp.data.remote

import com.example.weatherapp.data.models.ForecastResponseApi
import android.util.Log
import com.example.weatherapp.data.models.CityResponse
import com.example.weatherapp.data.models.CurrentResponseApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

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
                //delay(1000)
                Log.d(
                    "TAG",
                    "currentWeather: IN Remote Weather soruce ${
                        latestWeather.body()?.weather?.get(0)
                    }"
                )

                if (latestWeather.isSuccessful) {
                    val response = latestWeather.body()
                    if (response != null) {

                        emit(response)
                        Log.d(
                            "TAG",
                            "currentWeather: In isSuccessful and NOt Null Remote Weather soruce ${
                                latestWeather.body()?.weather?.get(0)
                            }"
                        )
                    } else {
                        Log.d(
                            "TAG",
                            "currentWeather: In isSuccessful but NULL  Remote Weather soruce ${
                                latestWeather.body()?.weather?.get(0)
                            }"
                        )
                    }

                } else {
                    Log.w("TAG", "currentWeather: FAlid")
                }

            } catch (e: Exception) {
                Log.d(
                    "TAG", "currentWeather: IN Remote Weather soruce ${e.message}"
                )
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
                //     delay(1000)

                Log.d(
                    "TAG",
                    "forecast: IN Remote Weather soruce ${latestWeather.body()?.list?.get(0)}"
                )

                if (latestWeather.isSuccessful) {
                    val response = latestWeather.body()
                    if (response != null) {

                        emit(response)
                        Log.d(
                            "TAG",
                            "forecast: In isSuccessful and NOt Null Remote Weather soruce ${
                                latestWeather.body()?.list?.get(0)
                            }"
                        )

                    } else {
                        Log.d(
                            "TAG",
                            "forecast: In isSuccessful but NULL  Remote Weather soruce ${
                                latestWeather.body()?.list?.get(0)
                            }"
                        )
                    }

                } else {
                    Log.w("TAG", "forecast: FAlid")
                }

            } catch (e: Exception) {
                Log.d(
                    "TAG", "forecast: IN Remote Weather soruce ${e.message}"
                )
            }

        }
    }

    override suspend fun getCityByLatLon(lat: Double, lon: Double): List<CityResponse.CityResponseItem> {
        return apiService.getLocationDetails(lat, lon)
    }


}

