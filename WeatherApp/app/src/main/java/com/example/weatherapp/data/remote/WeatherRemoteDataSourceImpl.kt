package com.example.weatherapp.data.remote

import ForecastResponseApi
import android.util.Log
import com.example.weatherapp.BuildConfig
import com.example.weatherapp.data.models.CurrentResponseApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response
import kotlin.coroutines.coroutineContext

class WeatherRemoteDataSourceImpl(private val apiService: WeatherApiService) :
    WeatherRemoteDataSource {
    private var  currentCachedWeather: CurrentResponseApi? = null
    private var  forecastCachedWeather: ForecastResponseApi? = null
    private var lastFetchTime: Long = 0


    override suspend fun currentWeather(
        lat: Double,
        lon: Double,
        lang: String

    ): Flow<CurrentResponseApi> {
            return flow {
                try {
                    val currentTime = System.currentTimeMillis()
                    if (currentCachedWeather != null && currentTime - lastFetchTime < 10 * 60 * 1000) {
                        // Use cached data
                        emit(currentCachedWeather!!)
                        Log.d("TAG", "Using cached weather data")
                    } else {

                        delay(2000)

                        val latestWeather =
                            apiService.getCurrentWeather(lat, lon, lang)

                        Log.d(
                            "TAG",
                            "currentWeather: IN Remote Weather soruce ${
                                latestWeather.body()?.weather?.get(0)}")

                        if (latestWeather.isSuccessful) {
                            val response = latestWeather.body()
                            if (response != null) {

                                currentCachedWeather = response
                                lastFetchTime = currentTime
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
                            Log.w("TAG", "currentWeather: FAlid",)
                        }
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
        lang: String
    ): Flow<ForecastResponseApi> {
        return flow {
            try {
                val currentTime = System.currentTimeMillis()
                if (forecastCachedWeather != null && currentTime - lastFetchTime < 10 * 60 * 1000) {
                    // Use cached data
                    emit(forecastCachedWeather!!)
                    Log.d("TAG", "forecast  Using cached weather data")
                } else {

                    delay(2000)

                    val latestWeather =
                        apiService.getForecastWeather(lat, lon, lang)

                    Log.d(
                        "TAG",
                        "forecast: IN Remote Weather soruce ${
                            latestWeather.body()?.list?.get(0)}")

                    if (latestWeather.isSuccessful) {
                        val response = latestWeather.body()
                        if (response != null) {

                            forecastCachedWeather = response
                            lastFetchTime = currentTime
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
                        Log.w("TAG", "forecast: FAlid",)
                    }
                }
            } catch (e: Exception) {
                Log.d(
                    "TAG", "forecast: IN Remote Weather soruce ${e.message}"
                )
            }

        }
    }


}

