package com.example.weatherapp.navigation

sealed class ScreenRoute(val route: String) {
    data object Home : ScreenRoute(NavRoutes.HOME_SCREEN)
    data object Favorites : ScreenRoute(NavRoutes.FAVORITE_SCREEN)
    data object Alerts : ScreenRoute(NavRoutes.ALERT_SCREEN)
    data object Settings : ScreenRoute(NavRoutes.SETTING_SCREEN)
}


object NavRoutes {
    const val HOME_SCREEN = "Home"
    const val FAVORITE_SCREEN = "Favorite"
    const val SETTING_SCREEN = "Setting"
    const val ALERT_SCREEN = "Alert"
}