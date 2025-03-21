package com.example.weatherapp

import android.location.LocationProvider
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp.data.models.CurrentResponseApi
import com.example.weatherapp.data.remote.RetrofitHelper
import com.example.weatherapp.data.remote.WeatherRemoteDataSourceImpl
import com.example.weatherapp.data.repository.WeatherRepositoryImpl
import com.example.weatherapp.screens.HomeScreen
import com.example.weatherapp.ui.theme.WeatherAppTheme
import com.example.weatherapp.utils.location.DefaultLocationClient
import com.example.weatherapp.utils.location.LocationClient
import com.example.weatherapp.viewmodel.CurrentWeatherFactory
import com.example.weatherapp.viewmodel.HomeScreenViewModel
import com.google.android.gms.location.LocationServices

class MainActivity : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val locationClient:LocationClient = DefaultLocationClient(applicationContext,LocationServices.getFusedLocationProviderClient(applicationContext))
        setContent {
            WeatherAppTheme {
                HomeScreen(
                    viewModel =
                    ViewModelProvider(
                        this,
                        CurrentWeatherFactory(
                            repo = WeatherRepositoryImpl.getInstance(
                                WeatherRemoteDataSourceImpl(
                                    RetrofitHelper.service
                                ),
                                locationClient
                            )
                        )
                    )[HomeScreenViewModel::class.java]
                )


            }
        }


    }
}

