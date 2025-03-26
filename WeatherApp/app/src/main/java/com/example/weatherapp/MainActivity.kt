package com.example.weatherapp

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.rememberNavController
import com.example.weatherapp.data.local.CityDatabase
import com.example.weatherapp.data.local.CityLocationLocalDataSourceImpl
import com.example.weatherapp.data.remote.RetrofitHelper
import com.example.weatherapp.data.remote.WeatherRemoteDataSourceImpl
import com.example.weatherapp.data.repository.WeatherRepositoryImpl
import com.example.weatherapp.favorite.FavoriteLocationFactory
import com.example.weatherapp.favorite.FavoriteScreenViewModel
import com.example.weatherapp.navigation.BottomNavigationBar
import com.example.weatherapp.navigation.SetupNavHost
import com.example.weatherapp.homescreen.viewmodel.CurrentWeatherFactory
import com.example.weatherapp.homescreen.viewmodel.HomeScreenViewModel

import com.example.weatherapp.utils.SharedObject

class MainActivity : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val homeFactory = CurrentWeatherFactory(
            repo = WeatherRepositoryImpl.getInstance(
                WeatherRemoteDataSourceImpl(
                    RetrofitHelper.service
                ),
                CityLocationLocalDataSourceImpl(
                    CityDatabase.getInstance(this@MainActivity).getCityDao()
                )
            )
        )
        val favoriteFactory = FavoriteLocationFactory(
            repo = WeatherRepositoryImpl.getInstance(
                WeatherRemoteDataSourceImpl(
                    RetrofitHelper.service
                ),
                CityLocationLocalDataSourceImpl(
                    CityDatabase.getInstance(this@MainActivity).getCityDao()
                )
            )
        )
        setContent {
            Log.w("TAG", "onCreate: share init")
            SharedObject.init(this)


            val viewModelHome = ViewModelProvider(this, homeFactory)[HomeScreenViewModel::class.java]
            val favoriteLocationViewModel = ViewModelProvider(this, favoriteFactory)[FavoriteScreenViewModel::class.java]


            val navController = rememberNavController()
            Scaffold(

                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 32.dp),

                bottomBar = { BottomNavigationBar(navController = navController) }
            ) { pad ->
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    SetupNavHost(navController = navController, viewModelHome,favoriteLocationViewModel)
                }

            }


        }


    }
}


