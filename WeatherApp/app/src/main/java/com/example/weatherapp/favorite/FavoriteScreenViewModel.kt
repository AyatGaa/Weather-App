package com.example.weatherapp.favorite

import com.example.weatherapp.data.models.ForecastItem
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.models.*
import com.example.weatherapp.data.repository.WeatherRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneOffset

class FavoriteScreenViewModel(private val repo: WeatherRepository) : ViewModel() {

    var currentLat by mutableStateOf(0.0)
    var currentLon by mutableStateOf(0.0)

    private val _mutableDatabaseMessage = MutableSharedFlow<String>()
    val mutableDatabaseMessage = _mutableDatabaseMessage.asSharedFlow()

    private val _localCityFlow = MutableStateFlow<List<CityLocation>>(emptyList())
    val localCityFlow = _localCityFlow.asStateFlow()


    fun updateLocation(lat: Double, lon: Double) {
        currentLat = lat
        currentLon = lon
    }


    private val _uiState = MutableStateFlow<ResponseState<CityLocation>>(ResponseState.Loading)
    val uiState: StateFlow<ResponseState<CityLocation>> = _uiState.asStateFlow()


    fun getLocationData(lat: Double, lon: Double) {
        viewModelScope.launch {
            try {
                val city = repo.getCityByLatLon(lat, lon).first()
                val weather = repo.getCurrentWeather(lat, lon, "en", "Standard").first()
                val forecast = repo.getForecastWeather(lat, lon, "en", "Standard").first()

                val uiState = CityLocation(
                    cityData = city,
                    lat = lat,
                    lon = lat,
                    currentWeather = weather,
                    forecastWeather = forecast,
                    flag = " "
                )

                _uiState.value = ResponseState.Success(uiState)
                addFavouriteLocation(uiState)
            } catch (e: Exception) {
                _uiState.value = ResponseState.Failure(e)
            }
        }

    }


    fun addFavouriteLocation(cityLocation: CityLocation) {

        viewModelScope.launch {
            try {
                val res = repo.insertCityLocation(cityLocation)

                if (res > 0) {
                    _mutableDatabaseMessage.emit("Location Added!")
                } else {

                    _mutableDatabaseMessage.emit("Can Not Add")
                }
            } catch (th: Throwable) {
                th.message?.let { _mutableDatabaseMessage.emit(it) }
            }
        }
    }

    fun getAllFavoriteLocationFromDataBase() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                delay(100)
                repo.getFavouriteCityLocations()
                    .catch {
                        _mutableDatabaseMessage.emit("Can not get data from db")
                    }.collect {

                        _localCityFlow.value += it
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

                    _localCityFlow.update { oldList ->
                        oldList.filterNot { it.lat == cityLocation.lat && it.lon == cityLocation.lon }
                    }
                    _mutableDatabaseMessage.emit("Location Deleted!")
                    getAllFavoriteLocationFromDataBase()
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
