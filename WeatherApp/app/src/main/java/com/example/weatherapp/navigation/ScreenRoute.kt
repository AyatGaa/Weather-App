package com.example.weatherapp.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.weatherapp.R
import kotlinx.serialization.Serializable

sealed class ScreenRoute(val route: String) {
    object Home : ScreenRoute(NavRoutes.homeScreen)
    object Favorites : ScreenRoute(NavRoutes.favoriteScreen)
    object Alerts : ScreenRoute(NavRoutes.alertScreen)
    object Settings : ScreenRoute(NavRoutes.settingScreen)
}


object NavRoutes {
    const val homeScreen = "Home"
    const val favoriteScreen = "Favorite"
    const val settingScreen = "Setting"
    const val alertScreen = "Alert"
}