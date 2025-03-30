package com.example.weatherapp.screens

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.models.ResponseState
import com.example.weatherapp.data.models.WeatherAlert
import com.example.weatherapp.data.repository.WeatherRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AlertViewModel(private val repo: WeatherRepository) : ViewModel() {
    private val _mutableDatabaseMessage = MutableSharedFlow<String>()
    val mutableDatabaseMessage = _mutableDatabaseMessage.asSharedFlow()

    private val _alertFlow = MutableStateFlow<List<WeatherAlert>>(emptyList())
    val alertFlow = _alertFlow.asStateFlow()


    private val _uiState = MutableStateFlow<ResponseState<WeatherAlert>>(ResponseState.Loading)
    val uiState = _uiState.asStateFlow()


    fun onTimeSelected(start: Long, end: Long){


    }

    fun addAlert(start: Long?, end: Long?) {
        viewModelScope.launch {
            try {
                if(start != null && end !=null) {
                    val alert = WeatherAlert(
                        startDate = start,
                        endDate = end,
                        cityName = "TEst Cit+",
                        lat = 0.0+1,
                        lon = 0.0
                    )


                    Log.i("TAG", "addAlert:  ")
                    val result = repo.insertAlert(alert)
                    if (result > 0) {
                        _alertFlow.update { oldList -> oldList + alert }
                        Log.i("TAG", "addAlert:  $alert")
                        _mutableDatabaseMessage.emit("Alert Added!")
                    } else {

                        _mutableDatabaseMessage.emit("Can Not Add")
                    }
                }else{

                        _mutableDatabaseMessage.emit("Time not Picked")
                }
            } catch (ex: Throwable) {
                _mutableDatabaseMessage.emit("Something Went Wrong")
            }

        }
    }


    fun deleteAlert(alert: WeatherAlert) {
        viewModelScope.launch {
            try {
                Log.d("TAG", "deleteAlert: ")
                val result = repo.deleteAlert(alert)
                if (result > 0) {
                    _alertFlow.update { oldList -> oldList - alert }
                    Log.d("TAG", "deleteAlert:${alert.toString()} ")
                    _mutableDatabaseMessage.emit("Alert Deleted!")
                } else {

                    _mutableDatabaseMessage.emit("Can Not Add")
                }

            } catch (ex: Throwable) {
                _mutableDatabaseMessage.emit("Something Went Wrong")

            }

        }

    }

    fun getAllAlerts() {
        viewModelScope.launch {
            try {
                Log.i("TAG", "getAllAlerts: ")
                repo.getAllWeatherAlert()
                    .catch {
                        _mutableDatabaseMessage.emit("Can not get data from db")
                    }.collectLatest {
                        _alertFlow.value = it
                        Log.i("TAG", "getAllAlerts: $it")
                    }

            } catch (ex: Throwable) {
                _mutableDatabaseMessage.emit("Something Went Wrong")

            }

        }
    }

}

class AlertFactory(private val repo: WeatherRepository) : ViewModelProvider.Factory {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AlertViewModel(repo) as T
    }
}
