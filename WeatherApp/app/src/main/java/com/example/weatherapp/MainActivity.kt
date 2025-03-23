package com.example.weatherapp

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.rememberNavController
import com.example.weatherapp.data.remote.RetrofitHelper
import com.example.weatherapp.data.remote.WeatherRemoteDataSourceImpl
import com.example.weatherapp.data.repository.WeatherRepositoryImpl
import com.example.weatherapp.navigation.BottomNavigationBar
import com.example.weatherapp.navigation.SetupNavHost
import com.example.weatherapp.homescreen.viewmodel.CurrentWeatherFactory
import com.example.weatherapp.homescreen.viewmodel.HomeScreenViewModel

class MainActivity : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            val viewModel = ViewModelProvider(
                this,
                CurrentWeatherFactory(
                    repo = WeatherRepositoryImpl.getInstance(
                        WeatherRemoteDataSourceImpl(
                            RetrofitHelper.service
                        )
                    )
                )
            )[HomeScreenViewModel::class.java]

            val navController = rememberNavController()
            Scaffold(

                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 32.dp),

                bottomBar = { BottomNavigationBar(navController = navController) }
            ) { pad ->
                Row   (modifier = Modifier
                    .fillMaxSize()
                   ) {
                    SetupNavHost(navController = navController, viewModel)
                }

            }


        }


    }
}


