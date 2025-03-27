package com.example.weatherapp.navigation

import com.google.android.gms.maps.model.LatLng
import kotlinx.serialization.Serializable


@kotlinx.serialization.Serializable
sealed class ScreenRoute() {

    @Serializable
    data object Home : ScreenRoute()

    @Serializable
    data class Favorites(val lat: Double =0.0, val lon: Double=0.0) : ScreenRoute()

    @Serializable
    data object Alerts : ScreenRoute()

    @Serializable
    data object Settings : ScreenRoute()

    @Serializable
    data object MapScreen : ScreenRoute()

}


object NavRoutes {
    const val HOME_SCREEN = "Home"
    const val FAVORITE_SCREEN = "Favorite"
    const val SETTING_SCREEN = "Setting"
    const val ALERT_SCREEN = "Alert"
    const val MAP_SCREEN = "Map"

}