package com.example.weatherapp.navigation

import androidx.compose.foundation.background
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.weatherapp.ui.theme.BabyBlue
import com.example.weatherapp.ui.theme.DarkBlue2
import com.example.weatherapp.ui.theme.White
import com.example.weatherapp.ui.theme.Yellow

data class NavigationItem(
    val title: String,
    //  val icon: ImageVector,
    val route: String,
    val selected: ImageVector,
    val unSelected: ImageVector
)

val navigationItems = listOf(
    NavigationItem(
        title = "Home",
        selected = Icons.Filled.Home,
        unSelected = Icons.Outlined.Home,
        route = ScreenRoute.Home.route
    ),
    NavigationItem(
        title = "Alert",
        selected = Icons.Filled.Notifications,
        unSelected = Icons.Outlined.Notifications,
        route = ScreenRoute.Alerts.route
    ),
    NavigationItem(
        title = "Favorite",
        selected = Icons.Filled.Favorite,
        unSelected = Icons.Outlined.Favorite,
        route = ScreenRoute.Favorites.route
    ),
    NavigationItem(
        title = "Setting",
        selected = Icons.Filled.Settings,
        unSelected = Icons.Outlined.Settings,
        route = ScreenRoute.Settings.route
    )
)

@Composable
fun BottomNavigationBar(navController: NavController) {
    val screens = listOf(
        ScreenRoute.Home,
        ScreenRoute.Alerts,
        ScreenRoute.Favorites,
        ScreenRoute.Settings
    )

    val currentDestination = navController.currentBackStackEntry?.destination?.route
    var currentDestination2 by rememberSaveable {
        mutableStateOf(0)
    }



    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 2.dp
        ,
    ) {
        navigationItems.forEachIndexed { index, item ->
            NavigationBarItem(
                selected = currentDestination2 ==index,
                onClick = {
                    currentDestination2 = index
                    navController.navigate(item.route)
                },
                icon = {
                    Icon(
                        imageVector = if ( index  == currentDestination2) {
                            item.selected
                        }else{ item.unSelected},
                        contentDescription = item.route,

                        tint = DarkBlue2
                    )
                },
                label = {
                    Text(
                        item.route,
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = BabyBlue,
                    indicatorColor = BabyBlue
                )
            )
        }
    }
}
