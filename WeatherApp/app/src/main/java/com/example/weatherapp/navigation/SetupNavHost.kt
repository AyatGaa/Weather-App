package com.example.weatherapp.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.weatherapp.screens.Alert
import com.example.weatherapp.screens.Favourite
import com.example.weatherapp.homescreen.view.HomeScreen
import com.example.weatherapp.screens.Setting
import com.example.weatherapp.homescreen.viewmodel.HomeScreenViewModel

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
