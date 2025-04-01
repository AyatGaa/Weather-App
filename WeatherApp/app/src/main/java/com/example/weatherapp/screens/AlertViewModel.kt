package com.example.weatherapp.screens

import android.content.Context
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.weatherapp.R
import com.example.weatherapp.data.models.ResponseState
import com.example.weatherapp.data.models.WeatherAlert
import com.example.weatherapp.data.repository.WeatherRepository
import com.example.weatherapp.utils.SharedObject
import com.example.weatherapp.utils.getWeatherIcon
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class AlertViewModel(private val repo: WeatherRepository) : ViewModel() {
    private val _mutableDatabaseMessage = MutableSharedFlow<String>()
    val mutableDatabaseMessage = _mutableDatabaseMessage.asSharedFlow()

    private val _alertFlow = MutableStateFlow<List<WeatherAlert>>(emptyList())
    val alertFlow = _alertFlow.asStateFlow()


    private val _uiState = MutableStateFlow<ResponseState<WeatherAlert>>(ResponseState.Loading)
    val uiState = _uiState.asStateFlow()


    fun scheduleNotification(context: Context, triggerTimeMillis: Long, alert: WeatherAlert) {
        viewModelScope.launch {


            val weather = repo.getCurrentWeather(alert.lat, alert.lon, "en", "Metric").first()

            val durationMillis = alert.endDate - alert.startDate  // Make sure duration is correctly calculated

            val notificationId = (System.currentTimeMillis() % Int.MAX_VALUE).toInt()  // Generate unique ID

            // Serialize the alert object to JSON
            val gson = Gson()
            val alertJson = gson.toJson(alert)

             val weatherData = workDataOf(
                "notificationId" to notificationId,
                "alertData" to alertJson,
                "des" to weather.weather?.get(0)?.description,
                "id" to alert.id
                )
            Log.w("TAG", "scheduleNotification: alert ID from MOdel ${alert.id}", )

            val now = System.currentTimeMillis()
            val delay = triggerTimeMillis - now

            if (delay > 0) {
                val workRequest = OneTimeWorkRequestBuilder<NotificationWorker>()
                    .setInputData(weatherData)
                    .setInitialDelay(delay, TimeUnit.MILLISECONDS)
                    .build()

                Log.w("TAG", "scheduleNotification: im here in scheduleNotification()")

                WorkManager.getInstance(context).enqueue(workRequest)
            } else {
                Toast.makeText(
                    context,
                    "Cannot schedule notification for past time!",
                    Toast.LENGTH_SHORT
                ).show()
            }
            if(now > alert.endDate){

            getAllAlerts()
            }
        }
    }

    fun onTimeSelected(context: Context, start: Long, alert: WeatherAlert) {

        viewModelScope.launch {

                scheduleNotification(context, start, alert)

            Log.w("TAG", "onTimeSelected: Alert from selcted ${alert.id}", )
        }

    }


    fun addAlert(alert: WeatherAlert) {
        viewModelScope.launch {
            try {

                val city = repo.getCityByLatLon(
                    alert.lat, alert.lon
                ).first()
                alert.cityName = city.name ?: "city"



                Log.i("TAG", "addAlert:  ")
                val result = repo.insertAlert(alert)

                if (result > 0) {
                    _alertFlow.update { oldList -> oldList + alert }
                    Log.i("TAG", "addAlert:  $alert")
                    _mutableDatabaseMessage.emit("Alert Added!")
                } else {

                    _mutableDatabaseMessage.emit("Can Not Add")
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
