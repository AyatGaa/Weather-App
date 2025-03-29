package com.example.weatherapp.homescreen.viewmodel

import com.example.weatherapp.data.models.ForecastItem
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.models.CurrentResponseApi
import com.example.weatherapp.data.models.ResponseState
import com.example.weatherapp.data.repository.WeatherRepository
import com.example.weatherapp.utils.SharedObject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch


// here where i send data to compose funs
@RequiresApi(Build.VERSION_CODES.O)
class HomeScreenViewModel(private val repo: WeatherRepository) : ViewModel() {

    private val _lang = mutableStateOf("en")
    var lang = _lang
    private val _unit = mutableStateOf("Standard")
    var unit = _unit
    private val _mapLat = mutableDoubleStateOf(0.0)
    var mapLat = _mapLat
    private val _mapLon = mutableDoubleStateOf(0.0)
    var mapLon = _mapLon


    private val _currentWeatherData =
        MutableStateFlow<ResponseState<CurrentResponseApi>>(ResponseState.Loading)
    var currentWeatherData = _currentWeatherData.asStateFlow()

    private val _hourlyWeatherData =
        MutableStateFlow<ResponseState<List<ForecastItem>>>(ResponseState.Loading)
    var hourlyWeatherData = _hourlyWeatherData.asStateFlow()

    private val _dailyWeatherData =
        MutableStateFlow<ResponseState<List<ForecastItem>>>(ResponseState.Loading)
    var dailyWeatherData = _dailyWeatherData.asStateFlow()


    //not used for now
    private val _mutableMessage = MutableSharedFlow<String>()
    val mutableMessage = _mutableMessage.asSharedFlow()

    fun getCurrentSetting() {
        viewModelScope.launch {
            val langRes = SharedObject.getString("lang", "en")
            val unitRes = SharedObject.getString("temp", "en")
            val locRes = SharedObject.getString("loc", "GPS")

            if (langRes == "Arabic") _lang.value = "ar" else {
                _lang.value = "en"
            }

            when (unitRes) {
                "Celsius" -> _unit.value = "metric"
                "Kelvin" -> _unit.value = "standard"
                "Fahrenheit" -> _unit.value = "imperial"
                else -> _unit.value = "standard"
            }

            if (locRes == "Map") {
                _mapLat.doubleValue = SharedObject.getString("lat", "0.0").toDouble()
                _mapLon.doubleValue = SharedObject.getString("lon", "0.0").toDouble()
            }
        }
    }
    /*
      * Standard => Kelvin -> meter/sec
      * Metric => Celsius => meter/sec
      * Imperial => Fahrenheit => mile/hour
      * */


    fun loadCurrentWeather(lat: Double, lon: Double, lang: String, units: String) {
        viewModelScope.launch {
            try {
                getCurrentSetting()
                val result = repo.getCurrentWeather(lat, lon, lang, units)
                result.catch { ex ->
                    _currentWeatherData.value = ResponseState.Failure(ex)
                    _mutableMessage.emit("API Error ${ex.message}")
                }.collect {

                    _currentWeatherData.value = ResponseState.Success(it)
                    _mutableMessage.emit("Done")
                }

            } catch (ex: Exception) {
                _currentWeatherData.value = ResponseState.Failure(ex)
                _mutableMessage.emit("Something Went Wrong, Try Again Later !")
            }
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun loadForecastWeather(lat: Double, lon: Double, lang: String, units: String) {

        viewModelScope.launch {
            getCurrentSetting()
            val result = repo.getForecastWeather(lat, lon, lang, units)
            result.catch { ex ->

                _dailyWeatherData.value = ResponseState.Failure(ex)
                _hourlyWeatherData.value = ResponseState.Failure(ex)
                _mutableMessage.emit("API Error ${ex.message}")

            }.collect { forecast ->
                val hourly = forecast.list.take(8)
                val daily = forecast.list.groupBy {
                    it.timestamp.let { ts ->
                        java.time.Instant.ofEpochSecond(ts)
                            .atZone(java.time.ZoneOffset.UTC)
                            .toLocalDate()
                    }
                }.map { (_, forecasts) ->
                    forecasts.first()
                }
                _hourlyWeatherData.value = ResponseState.Success(hourly)
                _dailyWeatherData.value = ResponseState.Success(daily)
            }
        }
    }
}


class CurrentWeatherFactory(private val repo: WeatherRepository) : ViewModelProvider.Factory {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HomeScreenViewModel(repo) as T
    }
}


