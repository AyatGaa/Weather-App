package com.example.weatherapp.data.models

import androidx.compose.ui.graphics.vector.ImageVector
import com.example.weatherapp.navigation.ScreenRoute
import kotlinx.serialization.Serializable

@Serializable
data class NavigationItem(
    val title: String,
    val route: ScreenRoute,
    val selected: Int,
    val unSelected: Int
)
