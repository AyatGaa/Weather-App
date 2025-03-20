package com.example.weatherapp.viewmodel

import ForecastItem
import ForecastResponseApi
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.models.CurrentResponseApi
import com.example.weatherapp.data.models.ResponseState
import com.example.weatherapp.data.repository.WeatherRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import retrofit2.Response


// here where i send data to compose funs
class HomeScreenViewModel(private val repo: WeatherRepository) : ViewModel() {

    private val _currentWeatherData =
        MutableStateFlow<ResponseState<CurrentResponseApi>>(ResponseState.Loading)
    var currentWeatherData = _currentWeatherData.asStateFlow()

    private val _hourlyWeatherData =
        MutableStateFlow<ResponseState<List<ForecastItem>>>(ResponseState.Loading)
    var hourlyWeatherData = _hourlyWeatherData.asStateFlow()

    private val _dailyWeatherData =
        MutableStateFlow<ResponseState<List<ForecastItem>>>(ResponseState.Loading)
    var dailyWeatherData = _dailyWeatherData.asStateFlow()

    private val _mutableMessage = MutableSharedFlow<String>()
    val mutableMessage = _mutableMessage.asSharedFlow()

    init {
        loadCurrentWeather(44.34, 10.99, "en")
        loadForecastWeather(44.34, 10.99, "en")
    }

    fun loadCurrentWeather(lat: Double, lon: Double, lang: String) {
        viewModelScope.launch {
            try {
                val result = repo.getCurrentWeather(lat, lon, lang)

                result.catch { ex ->

                    _currentWeatherData.value = ResponseState.Failure(ex)
                    _mutableMessage.emit("API Error ${ex.message}")
                }.collect {

                    _currentWeatherData.value = ResponseState.Success(it)
                }

            } catch (ex: Exception) {
                _currentWeatherData.value = ResponseState.Failure(ex)
                _mutableMessage.emit("Something Went Wrong, Try Again Later !")
            }
        }
    }


    fun loadForecastWeather(lat: Double, lon: Double, lang: String) {
        viewModelScope.launch {
            val result = repo.getForecastWeather(lat,lon,lang)
            result.catch {ex->
                    _dailyWeatherData.value = ResponseState.Failure(ex)
                    _hourlyWeatherData.value = ResponseState.Failure(ex)
                    _mutableMessage.emit("API Error ${ex.message}")
            }.collect{ forecast ->
                val hourly = forecast.list.take(8)
                val daily = forecast.list.groupBy {

                    it.timestamp.let {

                        ts -> java.time.Instant.ofEpochSecond(ts)
                            .atZone(java.time.ZoneOffset.UTC)
                            .toLocalDate()
                    }
                }.map {
                        (_,forecasts)->
                        forecasts.first()
                }
                _hourlyWeatherData.value = ResponseState.Success(hourly)
                _dailyWeatherData.value = ResponseState.Success(daily)

            }
        }
    }

}

class CurrentWeatherFactory(private val repo: WeatherRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HomeScreenViewModel(repo) as T
    }
}