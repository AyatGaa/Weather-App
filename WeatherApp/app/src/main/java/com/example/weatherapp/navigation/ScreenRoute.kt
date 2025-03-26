package com.example.weatherapp.navigation

import kotlinx.serialization.Serializable


@kotlinx.serialization.Serializable
sealed class ScreenRoute(val route: String) {
    @Serializable
    data object Home : ScreenRoute(NavRoutes.HOME_SCREEN)

    @Serializable
    data object Favorites : ScreenRoute(NavRoutes.FAVORITE_SCREEN)

    @Serializable
    data object Alerts : ScreenRoute(NavRoutes.ALERT_SCREEN)

    @Serializable
    data object Settings : ScreenRoute(NavRoutes.SETTING_SCREEN)

}


object NavRoutes {
    const val HOME_SCREEN = "Home"
    const val FAVORITE_SCREEN = "Favorite"
    const val SETTING_SCREEN = "Setting"
    const val ALERT_SCREEN = "Alert"

}