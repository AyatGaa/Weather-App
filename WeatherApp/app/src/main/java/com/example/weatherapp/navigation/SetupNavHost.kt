package com.example.weatherapp.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.createGraph
import androidx.navigation.toRoute
import com.example.weatherapp.data.remote.RetrofitHelper
import com.example.weatherapp.data.remote.WeatherRemoteDataSourceImpl
import com.example.weatherapp.data.repository.WeatherRepositoryImpl
import com.example.weatherapp.screens.Alert
import com.example.weatherapp.screens.Favourite
import com.example.weatherapp.screens.HomeScreen
import com.example.weatherapp.screens.Setting
import com.example.weatherapp.viewmodel.CurrentWeatherFactory
import com.example.weatherapp.viewmodel.HomeScreenViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SetupNavHost(navController: NavHostController, homeViewModel: HomeScreenViewModel) {
    NavHost(
        navController = navController,
        startDestination = ScreenRoute.Home.route
    ) {
        composable(ScreenRoute.Home.route) { HomeScreen(homeViewModel){} }
        composable(ScreenRoute.Favorites.route) { Favourite(){} }
        composable(ScreenRoute.Alerts.route) { Alert(){} }
        composable(ScreenRoute.Settings.route) { Setting(){} }
    }
}
