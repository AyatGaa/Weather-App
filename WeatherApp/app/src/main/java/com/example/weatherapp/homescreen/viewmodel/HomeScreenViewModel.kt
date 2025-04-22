package com.example.weatherapp.homescreen.viewmodel

import com.example.weatherapp.data.models.ForecastItem
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.models.CurrentResponseApi
import com.example.weatherapp.data.models.ForecastResponseApi
import com.example.weatherapp.data.models.HomeEntity
import com.example.weatherapp.data.models.ResponseState
import com.example.weatherapp.data.repository.WeatherRepository
import com.example.weatherapp.utils.SharedObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

data class WeatherState(
    val currentWeather: ResponseState<CurrentResponseApi> = ResponseState.Loading,
    val hourlyWeather: ResponseState<List<ForecastItem>> = ResponseState.Loading,
    val dailyWeather: ResponseState<List<ForecastItem>> = ResponseState.Loading
)

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



    private val _mutableMessage = MutableSharedFlow<String>()
    val mutableMessage = _mutableMessage.asSharedFlow()
    private val _weatherState = MutableStateFlow(WeatherState())
    val weatherState = _weatherState.asStateFlow()


    private fun updateWeatherState(
        currentWeather: ResponseState<CurrentResponseApi>? = null,
        hourlyWeather: ResponseState<List<ForecastItem>>? = null,
        dailyWeather: ResponseState<List<ForecastItem>>? = null
    ) {
        _weatherState.value = _weatherState.value.copy(
            currentWeather = currentWeather ?: _weatherState.value.currentWeather,
            hourlyWeather = hourlyWeather ?: _weatherState.value.hourlyWeather,
            dailyWeather = dailyWeather ?: _weatherState.value.dailyWeather
        )
    }

    private fun getCurrentSetting() {
        viewModelScope.launch {
            val unitRes = SharedObject.getString("temp", "Standard")
            val locRes = SharedObject.getString("loc", "GPS")

            _lang.value = SharedObject.getString("lang", "en")

            when (unitRes) {
                "Celsius", "درجة مئوية" -> _unit.value = "metric"
                "Kelvin", "كلفن" -> _unit.value = "standard"
                "Fahrenheit", "فهرنهايت" -> _unit.value = "imperial"
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


        private fun updateDatabase() {
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    val state = _weatherState.value

                    if (state.currentWeather is ResponseState.Success &&
                        state.hourlyWeather is ResponseState.Success &&
                        state.dailyWeather is ResponseState.Success
                    ) {
                        val homeEntity = HomeEntity(
                            0,
                            state.currentWeather.data,
                            state.hourlyWeather.data,
                            state.dailyWeather.data
                        )

                        repo.insertHomeData(homeEntity)
                    } else {
                       _mutableMessage.emit("Cache Failed!")
                    }

                } catch (ex: Exception) {
                    _mutableMessage.emit("Something Went Wrong with DB!")
                }
            }
        }

        private fun getHomeWeatherFromDatabase() {
            viewModelScope.launch(Dispatchers.IO) {
                val homeData = repo.getHomeData()
                homeData.let {
                    updateWeatherState(
                        currentWeather = ResponseState.Success(it.currentWeather),
                        hourlyWeather = ResponseState.Success(it.hourlyWeather),
                        dailyWeather = ResponseState.Success(it.dailyWeather)
                    )
                }
            }
        }
        fun loadCurrentWeather(lat: Double, lon: Double, lang: String, units: String) {
            viewModelScope.launch {
                try {
                    getCurrentSetting()
                    val result = repo.getCurrentWeather(lat, lon, lang, units)
                    result.catch { ex ->
                        updateWeatherState(currentWeather = ResponseState.Failure(ex))
                        _mutableMessage.emit("API Error ${ex.message}")
                        getHomeWeatherFromDatabase()
                    }.collect { weather ->
                        updateWeatherState(currentWeather = ResponseState.Success(weather))
                        _mutableMessage.emit("Done")
                        updateDatabase()
                    }
                } catch (ex: Exception) {
                    updateWeatherState(currentWeather = ResponseState.Failure(ex))
                    _mutableMessage.emit("Something Went Wrong, Try Again Later!")
                }
            }
        }

    @RequiresApi(Build.VERSION_CODES.O)
    fun loadForecastWeather(lat: Double, lon: Double, lang: String, units: String) {
        viewModelScope.launch {

            getCurrentSetting()

            val result = repo.getForecastWeather(lat, lon, lang, units)

            result.catch { ex ->

                updateWeatherState(hourlyWeather = ResponseState.Failure(ex), dailyWeather = ResponseState.Failure(ex))
                _mutableMessage.emit("API Error ${ex.message}")
                getHomeWeatherFromDatabase()

            }.collect { forecast ->

                val hourly = forecast.list.take(8)

                val daily = forecast.list.groupBy {
                    java.time.Instant.ofEpochSecond(it.timestamp)
                        .atZone(java.time.ZoneOffset.UTC)
                        .toLocalDate()
                }.map { (_, forecasts) -> forecasts.first() }

                updateWeatherState(
                    hourlyWeather = ResponseState.Success(hourly),
                    dailyWeather = ResponseState.Success(daily)
                )
                updateDatabase()
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


