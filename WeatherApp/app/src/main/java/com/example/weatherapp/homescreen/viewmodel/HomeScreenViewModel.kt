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
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import kotlin.math.log


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


    private val _localForecastHomeData = mutableStateOf(ForecastResponseApi(emptyList()))

   private val  _offlineHome = mutableStateOf(HomeEntity(0,null,null))


    //not used for now
    private val _mutableMessage = MutableSharedFlow<String>()
    val mutableMessage = _mutableMessage.asSharedFlow()


    fun getCurrentSetting() {
        viewModelScope.launch {
            val langRes = SharedObject.getString("lang", "en")
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
    var weather = mutableStateOf<CurrentResponseApi?>(null)

    fun getFromDataBase():HomeEntity {
        var result :HomeEntity = HomeEntity(0,null,null)
        viewModelScope.launch {
            try {
                result = repo.getHomeData()
            } catch (ex: Exception) {
                Log.i("TAG", "updateDatabase: $ex")
            }
        }
        return result
    }

    fun updateDatabase() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                if (weather.value == null || _localForecastHomeData.value.list.isEmpty()) {
                    Log.e("Database", "updateDatabase: Weather or Forecast data is NULL. Skipping insert.")
                    return@launch
                }

                val homeEntity = HomeEntity(0, weather.value!!, _localForecastHomeData.value)
                repo.insertHomeData(homeEntity)
                Log.d("Database", "Data Inserted Successfully: $homeEntity")

            } catch (ex: Exception) {
                Log.e("Database", "updateDatabase Error: ${ex.message}")
            }
        }
    }



    fun loadOnlineData(lat: Double, lon: Double, lang: String, units: String) {
        viewModelScope.launch {
            try {

                repo.getForecastWeather(lat, lon, lang, units).collect {

                }

            } catch (e: Exception) {

            }
        }


    }


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
                    updateDatabase()
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
                _localForecastHomeData.value = forecast
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


