package com.example.weatherapp.favorite.viewModel

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.models.*
import com.example.weatherapp.data.repository.WeatherRepository
import com.example.weatherapp.utils.SharedObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow


class FavoriteScreenViewModel(private val repo: WeatherRepository) : ViewModel() {

    private val _mutableDatabaseMessage = MutableSharedFlow<String>()
    val mutableDatabaseMessage = _mutableDatabaseMessage.asSharedFlow()

    private val _localCityFlow = MutableStateFlow<List<CityLocation>>(emptyList())
    val localCityFlow = _localCityFlow.asStateFlow()

    var isFetchingLocation by mutableStateOf(false)

    private val _uiState = MutableStateFlow<ResponseState<CityLocation>>(ResponseState.Loading)
    val uiState: StateFlow<ResponseState<CityLocation>> = _uiState.asStateFlow()

    private val _lang = mutableStateOf("en")
    var lang = _lang
    private val _unit = mutableStateOf("Standard")
    var unit = _unit

    init {
        getCurrentSetting()
    }

    fun getCurrentSetting() {
        viewModelScope.launch {
            val langRes = SharedObject.getString("lang", "en")
            val unitRes = SharedObject.getString("temp", "en")
            _lang.value = langRes

            when (unitRes) {
                "Celsius", "درجة مئوية" -> _unit.value = "metric"
                "Kelvin", "كلفن" -> _unit.value = "standard"
                "Fahrenheit", "فهرنهايت" -> _unit.value = "imperial"
                else -> _unit.value = "standard"
            }

        }
    }

    fun getLocationDetailsForCardOffline(lat: Double, lon: Double, id: Int) {
        // if offline get from db
        viewModelScope.launch {
            getCurrentSetting()
            Log.d("INSERT_DEBUG", "getLocationData() called for lat: $lat, lon: $lon")

            try {
                Log.i("fav", "getLocationDetailsForCardOffline: get data off")
                val cityDb = repo.getCityById(id)
                val uiState = CityLocation(
                    cityData = cityDb.cityData,
                    lat = lat,
                    lon = lon,
                    currentWeather = cityDb.currentWeather,
                    forecastWeather = cityDb.forecastWeather,
                    flag = cityDb.flag
                )
                Log.i("fav", "getLocationDetailsForCardOffline: get data off ${uiState}")
                _localCityFlow
                _uiState.value = ResponseState.Success(uiState)

            } catch (e: Exception) {
                _uiState.value = ResponseState.Failure(e)
            }
        }
    }

    fun getLocationDetailsForCardOnline(lat: Double, lon: Double) {
        // if online get form api,
        // if offline get from db
        //if online
        viewModelScope.launch {
            getCurrentSetting()
            Log.d("INSERT_DEBUG", "getLocationData() called for lat: $lat, lon: $lon")

            try {
                Log.i("fav", "getLocationDetailsForCardOnline: ${lat} $lon")
                val city = repo.getCityByLatLon(lat, lon).first()
                val weather = repo.getCurrentWeather(lat, lon, lang.value, unit.value).first()
                val forecast = repo.getForecastWeather(lat, lon, lang.value, unit.value).first()

                val uiState = CityLocation(
                    cityData = city,
                    lat = lat,
                    lon = lon,
                    currentWeather = weather,
                    forecastWeather = forecast,
                    flag = " "
                )

                Log.i("fav", "getLocationDetailsForCardOnline: ${uiState}")
                _uiState.value = ResponseState.Success(uiState)
                addFavouriteLocation(uiState)
            } catch (e: Exception) {
                _uiState.value = ResponseState.Failure(e)
            }
        }
    }


    fun getLocationData(lat: Double, lon: Double) {
        Log.i("fav", "getLocationData: $lat //  $lon")
// if (isFetchingLocation) return
//         isFetchingLocation = true
        viewModelScope.launch {
            Log.d("fav", "getLocationData() called for lat: $lat, lon: $lon")
            try {
                val weather = repo.getCurrentWeather(lat, lon, lang.value, unit.value).first()
                val forecast = repo.getForecastWeather(lat, lon, lang.value, unit.value).first()
                val city = repo.getCityByLatLon(lat, lon).first()

                val uiState = CityLocation(
                    cityData = city,
                    lat = lat,
                    lon = lon,
                    currentWeather = weather,
                    forecastWeather = forecast,
                    flag = " "
                )

                _uiState.value = ResponseState.Success(uiState)
                Log.w("Fav", "getLocationData: BEFORe inserting $uiState", )
                addFavouriteLocation(uiState)
                Log.w("Fav", "getLocationData: AFTER inserting $uiState", )


            } catch (e: Exception) {
                _uiState.value = ResponseState.Failure(e)
            } finally {
                isFetchingLocation = false
            }
        }

    }


    fun addFavouriteLocation(cityLocation: CityLocation) {
        Log.d("DatabaseInsert", "Adding city: ${cityLocation.cityData.name}")
        viewModelScope.launch {
            Log.d(
                "INSERT_DEBUG",
                "addFavouriteLocation() called for lat: ${cityLocation.lat}, lon: ${cityLocation.lon}"
            )

            try {
                    Log.d("fav", "Insertion result: ")

                val existingCities =
                    repo.getFavouriteCityLocations().first()
                val exists =
                    existingCities.any { it.lat == cityLocation.lat && it.lon == cityLocation.lon }
                if (!exists) {
                    val res = repo.insertCityLocation(cityLocation)
                    Log.d("fav", "Insertion result: $res")

                    if (res > 0) {
                        _localCityFlow.update { oldList -> oldList + cityLocation }
                        _mutableDatabaseMessage.emit("Location Added!")
                    } else {

                        _mutableDatabaseMessage.emit("Can Not Add")
                    }
                } else {
                    Log.d(
                        "INSERT_DEBUG",
                        "Location already exists, skipping insertion."
                    )
                    _mutableDatabaseMessage.emit("Location already exists!")
                }
            } catch (th: Throwable) {
                th.message?.let { _mutableDatabaseMessage.emit(it) }
            }
        }
    }

    fun getAllFavoriteLocationFromDataBase() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repo.getFavouriteCityLocations()
                    .catch {
                        _mutableDatabaseMessage.emit("Can not get data from db")
                    }.collectLatest {
                        _localCityFlow.value = it
                    }
            } catch (th: Throwable) {
                th.message?.let { _mutableDatabaseMessage.emit(it) }
            }
        }
    }


    fun deleteFromFavorite(cityLocation: CityLocation) {

        viewModelScope.launch {
            try {
                val res = repo.deleteCityLocation(cityLocation)
                delay(100)
                if (res >= 0) {

                    getAllFavoriteLocationFromDataBase()
                    _mutableDatabaseMessage.emit("Location Deleted!")
                    Log.d("DELETE_DEBUG", "Updated localCityFlow: ${_localCityFlow.value}")
                } else {
                    _mutableDatabaseMessage.emit("Couldn't delete ")
                }
            } catch (th: Throwable) {
                th.message?.let { _mutableDatabaseMessage.emit(it) }
            }

        }
    }
}

class FavoriteLocationFactory(private val repo: WeatherRepository) : ViewModelProvider.Factory {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return FavoriteScreenViewModel(repo) as T
    }
}
