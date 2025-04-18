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
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.weatherapp.R
import com.example.weatherapp.data.models.NavigationItem
import com.example.weatherapp.ui.theme.DarkBlue2
import com.example.weatherapp.ui.theme.White
import com.example.weatherapp.ui.theme.Yellow

var navigationItems: List<NavigationItem> = listOf(
    NavigationItem(
        title = "Home",
        selected = R.drawable.home_fill,
        unSelected = R.drawable.home_outline,
        route = ScreenRoute.Home
    ),
    NavigationItem(
        title = "Alert",
        selected = R.drawable.alert_fill,
        unSelected = R.drawable.alert_outline,
        route = ScreenRoute.Alerts
    ),
    NavigationItem(
        title = "Favorites",
        selected = R.drawable.fav_fill,
        unSelected = R.drawable.fav_outline,
        route = ScreenRoute.Favorites(0.0, 0.0)
    ),
    NavigationItem(
        title = "Setting",
        selected = R.drawable.setting_fill,
        unSelected = R.drawable.setting_outline,
        route = ScreenRoute.Settings
    )
)


@Composable
fun NavigationBar() {
    val homeTitle = stringResource(id = R.string.home)
    val alertTitle = stringResource(id = R.string.alert)
    val settingTitle = stringResource(id = R.string.setting)
    val favoriteTitle = stringResource(id = R.string.favorites)

    navigationItems = listOf(
        NavigationItem(
            title = homeTitle,
            selected = R.drawable.home_fill,
            unSelected = R.drawable.home_outline,
            route = ScreenRoute.Home
        ),
        NavigationItem(
            title = alertTitle,
            selected = R.drawable.alert_fill,
            unSelected = R.drawable.alert_outline,
            route = ScreenRoute.Alerts
        ),
        NavigationItem(
            title = favoriteTitle,
            selected = R.drawable.fav_fill,
            unSelected = R.drawable.fav_outline,
            route = ScreenRoute.Favorites(0.0, 0.0)
        ),
        NavigationItem(
            title = settingTitle,
            selected = R.drawable.setting_fill,
            unSelected = R.drawable.setting_outline,
            route = ScreenRoute.Settings
        )
    )
}


@Composable
fun BottomNavigationBar(navController: NavController) {
    val navStackBackEntry by navController.currentBackStackEntryAsState()
    val currentDestination3 = navStackBackEntry?.destination
    NavigationBar()
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
            AddItem(item, currentDestination3) {
                navController.navigate(item.route) {
                    popUpTo(navController.graph.findStartDestination().id) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            }
        }
    }
}

@Composable
fun AddItem(
    item: NavigationItem,
    currentDestination: NavDestination?,
    onNavigateItem: () -> Unit
) {

    val selected = isRouteSelected(item.route, currentDestination)
    val container = if (selected) Yellow else White

    val background = if (selected) DarkBlue2.copy(alpha = 0.3f) else Color.Transparent
    Box(
        modifier = Modifier
            .height(40.dp)
            .clip(shape = CircleShape)
            .background(background)
            .clickable(onClick = {
                onNavigateItem()
            })
    ) {
        Row(
            modifier = Modifier
                .padding(start = 10.dp, end = 10.dp, top = 8.dp, bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                painter = painterResource(if (selected) item.selected else item.unSelected),
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


fun isRouteSelected(route: ScreenRoute, currentDestination: NavDestination?): Boolean {
    val currentRoute = currentDestination?.route ?: return false
    return when (route) {
        is ScreenRoute.Home -> currentRoute.contains(NavRoutes.HOME_SCREEN)
        is ScreenRoute.Alerts -> currentRoute.contains(NavRoutes.ALERT_SCREEN)
        is ScreenRoute.Settings -> currentRoute.contains(NavRoutes.SETTING_SCREEN)
        is ScreenRoute.MapScreen -> currentRoute.contains(NavRoutes.MAP_SCREEN)
        is ScreenRoute.Favorites -> currentRoute.contains(NavRoutes.FAVORITE_SCREEN)
        is ScreenRoute.FavoriteCardDetails -> currentRoute.contains(NavRoutes.FAVORITE_CARD_DETAILS)
    }
}
