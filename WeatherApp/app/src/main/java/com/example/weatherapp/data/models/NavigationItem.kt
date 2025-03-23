package com.example.weatherapp.data.models

import androidx.compose.ui.graphics.vector.ImageVector

data class NavigationItem(
    val title: String,
    val route: String,
    val selected: ImageVector,
    val unSelected: ImageVector
)
