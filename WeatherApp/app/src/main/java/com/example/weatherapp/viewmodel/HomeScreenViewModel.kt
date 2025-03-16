package com.example.weatherapp.viewmodel

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.models.CurrentResponseApi
import com.example.weatherapp.data.repository.WeatherRepository
import kotlinx.coroutines.launch


// here where i send data to compose funs
class HomeScreenViewModel(private val repo: WeatherRepository) : ViewModel() {
    private val _currentWeatherData: MutableState<CurrentResponseApi?> = mutableStateOf(null)
    var currentWeatherData: MutableState<CurrentResponseApi?> = _currentWeatherData


    fun loadCurrentWeather(lat: Double, lon: Double, units: String, lang: String) {
        viewModelScope.launch  {

            val response = repo.getCurrentWeather(lat, lon, units, lang)
            if (response.isSuccessful) {
                currentWeatherData.value = response.body()
                Log.i("TAG", "loadCurrentWeather: ${currentWeatherData.value?.weather?.size}")
            } else {
                Log.e("HomeScreenViewModel", "API call failed: ${response.errorBody()}")
            }
        }
    }


}

class CurrentWeatherFactory(private val repo: WeatherRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HomeScreenViewModel(repo) as T
    }
}