package com.example.weatherapp.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.weatherapp.data.models.NavigationItem
import com.example.weatherapp.ui.theme.DarkBlue2
import com.example.weatherapp.ui.theme.White
import com.example.weatherapp.ui.theme.Yellow


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
        unSelected = Icons.Outlined.FavoriteBorder,
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
    val navStackBackEntry by navController.currentBackStackEntryAsState()
    val currentDestination3 = navStackBackEntry?.destination
    Row(

        modifier = Modifier
            .padding(start = 15.dp, end = 15.dp, top = 20.dp, bottom = 20.dp)
            .clip(CircleShape)
            .background(DarkBlue2)
            .padding(start = 4.dp, end = 4.dp, top = 8.dp, bottom = 8.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        navigationItems.forEachIndexed { _, item ->
            AddItem(item, currentDestination3, navController)
        }
    }
}
/*
    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 2.dp
        ,
    ) {
        navigationItems.forEachIndexed { index, item ->
            AddItem(item, currentDestination2,navController)


           /* NavigationBarItem(
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
            )*/
        }
    }
}*/

@Composable
fun AddItem(
    item: NavigationItem,
    currentDestination: NavDestination?,
    navController: NavController
) {

    val selected = currentDestination?.hierarchy?.any { it.route == item.route } == true

    val container = if (selected) Yellow else White

    val background = if (selected) DarkBlue2.copy(alpha = 0.3f) else Color.Transparent
    Box(
        modifier = Modifier
            .height(40.dp)
            .clip(shape = CircleShape)
            .background(background)
            .clickable(onClick = {
                navController.navigate(item.route) {
                    popUpTo(navController.graph.findStartDestination().id)
                    launchSingleTop = true
                }
            })
    ) {
        Row(
            modifier = Modifier
                .padding(start = 10.dp, end = 10.dp, top = 8.dp, bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                imageVector = if (selected) item.selected else item.unSelected,
                contentDescription = item.title,
                tint = container
            )
            AnimatedVisibility(visible = selected) {
                Text(
                    text = item.title, color = container
                )
            }

        }
    }


}

