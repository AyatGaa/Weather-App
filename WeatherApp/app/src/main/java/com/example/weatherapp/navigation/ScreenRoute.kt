package com.example.weatherapp.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.weatherapp.R

sealed class ScreenRoute(val route: String, val title: String, val icon: ImageVector ){


     object Home : ScreenRoute("home", "Home", Icons.Outlined.Home)
    object Favorites : ScreenRoute("favorites", "Favorites", Icons.Outlined.Favorite)
    object Alerts : ScreenRoute("alerts", "Alerts",Icons.Outlined.Notifications)
    object Settings : ScreenRoute("settings", "Settings",  Icons.Outlined.Settings)
}