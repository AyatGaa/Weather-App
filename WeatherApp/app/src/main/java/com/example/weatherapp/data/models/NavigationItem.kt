package com.example.weatherapp.data.models

import com.example.weatherapp.navigation.ScreenRoute
import kotlinx.serialization.Serializable

@Serializable
data class NavigationItem(
    val title: String,
    val route: ScreenRoute,
    val selected: Int,
    val unSelected: Int
)
