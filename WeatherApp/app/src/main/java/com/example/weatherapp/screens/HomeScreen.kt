package com.example.weatherapp.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weatherapp.R
import com.example.weatherapp.navigation.ScreenRoute
import com.example.weatherapp.ui.theme.BabyBlue
import com.example.weatherapp.ui.theme.DarkBlue1
import com.example.weatherapp.ui.theme.DarkBlue2
import com.example.weatherapp.ui.theme.White
import com.example.weatherapp.ui.theme.Yellow

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen()
}

@Composable
fun HomeScreen() {
    var currentScreen by remember { mutableStateOf<ScreenRoute>(ScreenRoute.Home) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = BabyBlue,
        bottomBar = {
            BottomNavigationBar(
                currentScreen = currentScreen,
                onItemClick = { screen -> currentScreen = screen }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            // Top Section
            item {
                TopSection()
            }

            // Weather Details
            item {
                WeatherDetails()
            }


            // Hourly Forecast
            item {
                HourlyForecast()
            }
            // Daily Forecast
            item {
                DailyForecast()
            }
        }
    }
}


@Composable
fun BottomNavigationBar(   currentScreen: ScreenRoute,
                           onItemClick: (ScreenRoute) -> Unit) {


    val items = listOf(
        ScreenRoute.Home,
        ScreenRoute.Favorites,
        ScreenRoute.Alerts,
        ScreenRoute.Settings
    )

        NavigationBar(
            modifier = Modifier.background(White).shadow(4.dp)
        ) {
            items.forEachIndexed  {index, item ->
                NavigationBarItem(
                    selected = currentScreen ==item ,
                    onClick = {  onItemClick(item) },
                    icon = {   Icon(
                        imageVector =item.icon ,
                        modifier = Modifier.size(24.dp),
                        contentDescription = item.title
                    ) },
                    label = { Text(
                        text = item.title
                    )}

                )
            }
        }
    
}

@Composable
fun DailyForecast() {
    val dailyForecasts = listOf(
        "13/3 Today 15° 24°",
        "13/3 Today 15° 24°",
        "13/3 Today 15° 24°",
        "13/3 Today 15° 24°",
        "13/3 Today 15° 24°",
        "13/3 Today 15° 24°",
        "13/3 Today 15° 24°",
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {

        Text(
            text = "Next 5 Days",
            fontWeight = FontWeight.ExtraBold,
            style = MaterialTheme.typography.headlineMedium,
            color = White,
            fontSize = 24.sp,
        )
        Spacer(modifier = Modifier.height(8.dp))
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxHeight()
            .height(400.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(dailyForecasts.size) { forecast ->
            DailyForecastItem(
                date = "15/2",
                icon = R.drawable.water_drop,
                temp = "23",
                measurement = "°C"
            )
        }
    }
}

@Composable
fun DailyForecastItem(date: String, icon: Int, temp: String, measurement: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .shadow(4.dp, shape = RoundedCornerShape(8.dp))
            .clip(RoundedCornerShape(8.dp)),
        shape = RoundedCornerShape(8.dp),
    ) {
        Row(
            modifier = Modifier
                .background(White)
                .fillMaxWidth()
                .padding(28.dp),
            horizontalArrangement = Arrangement.SpaceBetween,

            ) {
            // Date
            Text(
                text = date,
                fontSize = 16.sp,
                color = DarkBlue2
            )
            Text(
                text = "Today",
                fontSize = 16.sp,
                color = DarkBlue2
            )

            // Icon
            Icon(
                painter = painterResource(id = icon),
                contentDescription = date,
                modifier = Modifier.size(24.dp)
            )

            // Temperature and Measurement
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = temp,
                    color = DarkBlue1
                )
                Text(
                    text = measurement,
                    color = DarkBlue1
                )
            }
        }
    }
}

@Composable
fun HourlyForecast() {
    val hourlyTemps = listOf("26°", "26°", "26°", "26°", "26°")


    Column(
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = "Hourly Details",
            fontWeight = FontWeight.ExtraBold,
            style = MaterialTheme.typography.headlineMedium,
            color = White,
            fontSize = 24.sp,
        )
        Spacer(modifier = Modifier.height(8.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),

            ) {
            items(hourlyTemps.size) {
                HourlyForecastItem("now", R.drawable.water_drop, "23", "oC")
            }
        }
    }
}

@Composable
fun HourlyForecastItem(time: String, icon: Int, temp: String, measurement: String) {
    Card(
        modifier = Modifier
            .width(80.dp)
            .height(100.dp)
            .shadow(4.dp, shape = RoundedCornerShape(8.dp))
            .clip(RoundedCornerShape(8.dp)),
        shape = RoundedCornerShape(8.dp),

        ) {
        Column(
            modifier = Modifier
                .background(White)
                .width(80.dp)
                .height(100.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {

            Text(
                text = time,
                fontSize = 16.sp,
                color = DarkBlue2,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .padding(bottom = 8.dp)
            )

            Icon(
                painter = painterResource(id = icon),
                contentDescription = time,
                modifier = Modifier
                    .size(24.dp)
                    .padding(bottom = 8.dp)
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = temp,
                    color = DarkBlue1
                )
                Text(
                    text = measurement,
                    color = DarkBlue1
                )
            }
        }
    }
}

@Composable
fun TopSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(BabyBlue),

        horizontalAlignment = Alignment.CenterHorizontally,

        ) {
        //location
        Text(
            text = "Egypt",
            fontWeight = FontWeight.Bold,
            fontSize = 28.sp,
            color = White
        )


        //data and time
        Text(
            text = "Thu’s mar 22 15 PM",
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            color = White
        )


        Row {
            Text(
                text = "${26}",
                fontWeight = FontWeight.Bold,
                fontSize = 48.sp,
                color = White
            )
            //temp type K, C, F
            Text(
                text = "°C",
                fontWeight = FontWeight.Bold,
                fontSize = 42.sp,
                color = White
            )
        }


        //icon
        //"https://openweathermap.org/img/wn/${weatherData.weather[0].icon}@4x.png"
        Image(
            painter = painterResource(id = R.drawable.cloud),

            contentDescription = "Weather Icon",
            modifier = Modifier
                .size(120.dp)
        )

        Text(
            text = "Cloudy",
            color = White
        )
        Text(
            text = "Clouds: 123 %",
            color = White
        )

    }
}


@Preview(showBackground = true)
@Composable
fun WeatherDetails() {
    Column(
        modifier = Modifier
            .padding(0.dp)
    ) {

        Text(
            text = "Weather Details",
            fontWeight = FontWeight.ExtraBold,
            color = White,
            fontSize = 24.sp,

            )
    }
    Spacer(modifier = Modifier.height(8.dp))
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(
            16.dp,
            alignment = Alignment.CenterHorizontally
        )

    ) {

        DetailedWeatherItem(label = "Pressure", value = "55", "hPa", icon = R.drawable.barometer)
        DetailedWeatherItem(label = "Humidity", value = "89", "%", icon = R.drawable.water_drop)
        DetailedWeatherItem(label = "Wind", value = "55", "Km/h", icon = R.drawable.wind)

    }

}


@Composable
fun DetailedWeatherItem(label: String, value: String, measurement: String, icon: Int) {
    Card(
        modifier = Modifier
            .width(100.dp)
            .height(120.dp)
            .shadow(4.dp, shape = RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .background(Yellow)
                .width(100.dp)
                .height(120.dp)
                .padding(0.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,

            ) {
            // Icon
            Icon(
                painter = painterResource(id = icon),
                contentDescription = label,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            // Label
            Text(
                text = label,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = DarkBlue2
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Value and Measurement
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = value,
                    color = DarkBlue1
                )
                Text(
                    text = measurement,
                    color = DarkBlue1
                )
            }
        }
    }
}