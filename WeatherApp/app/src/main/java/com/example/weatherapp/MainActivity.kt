package com.example.weatherapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import com.example.weatherapp.screens.HomeScreenPreview
import com.example.weatherapp.ui.theme.WeatherAppTheme
import com.example.weatherapp.viewmodel.CurrentWeatherFactory
import com.example.weatherapp.viewmodel.HomeScreenViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WeatherAppTheme {
                HomeScreenPreview()

          /*      Greeting(
                    viewModel =
                    ViewModelProvider(
                        this,
                        CurrentWeatherFactory(
                            repo = WeatherRepositoryImpl.getInstance(
                                WeatherRemoteDataSourceImpl(
                                    RetrofitHelper.service)
                            )
                        )
                    ).get(HomeScreenViewModel::class.java)
                )*/
            }
        }


    }
}

@Composable
fun Greeting(modifier: Modifier = Modifier, viewModel: HomeScreenViewModel) {
    // Observe the state from the ViewModel
    val weatherData = viewModel.currentWeatherData.value

    // Trigger API call when the composable is first launched
    LaunchedEffect(Unit) {
        viewModel.loadCurrentWeather(44.34, 10.99, "metric", "en")
    }

    // Display the weather data
    if (weatherData != null) {
        WeatherContent(weatherData, modifier)
    } else {
        Text(text = "Loading...", modifier = modifier.padding(16.dp))
    }
}

@Composable
fun WeatherContent(weatherData: CurrentResponseApi, modifier: Modifier = Modifier) {
   Column {
       Text(
           text = "Temperature: ${weatherData.main?.temp}°C",
           modifier = modifier.padding(16.dp)
       )
       Spacer(modifier = Modifier.size(5.dp))
       Text(
           text = "Temperature: ${weatherData.main?.humidity}Pha",
           modifier = modifier.padding(16.dp)
       )
       Spacer(modifier = Modifier.size(5.dp))
       Text(
           text = "Temperature: ${weatherData.main?.pressure} ppp",
           modifier = modifier.padding(16.dp)
       )
       Spacer(modifier = Modifier.size(5.dp))
       Text(
           text = "Temperature: ${weatherData.weather?.get(0)?.description} wather",
           modifier = modifier.padding(16.dp)
       )
       Spacer(modifier = Modifier.size(5.dp))
       Text(
           text = "Date: ${weatherData.timezone}  ",
           modifier = modifier.padding(16.dp)
       )
   }

}