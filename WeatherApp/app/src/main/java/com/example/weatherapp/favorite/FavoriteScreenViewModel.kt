package com.example.weatherapp.favorite

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.models.CityLocation
import com.example.weatherapp.data.repository.WeatherRepository
import com.example.weatherapp.homescreen.viewmodel.HomeScreenViewModel
import kotlinx.coroutines.launch

class FavoriteScreenViewModel(private val repo:WeatherRepository):ViewModel(){

    var currentLat by mutableStateOf(0.0)
    var currentLon by mutableStateOf(0.0)

    fun updateLocation(lat: Double, lon: Double) {
        currentLat = lat
        currentLon = lon
        // You might also save to database here
    }



    fun addFavouriteLocation(cityLocation: CityLocation){
        viewModelScope.launch {

            repo.insertCityLocation(cityLocation)


        }
    }



}


class FavoriteLocationFactory(private val repo: WeatherRepository) : ViewModelProvider.Factory {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return FavoriteScreenViewModel(repo) as T
    }
}
