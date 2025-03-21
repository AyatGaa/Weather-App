package com.example.weatherapp.screens

import ForecastItem
import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weatherapp.R
import com.example.weatherapp.navigation.ScreenRoute
import com.example.weatherapp.ui.theme.BabyBlue
import com.example.weatherapp.ui.theme.DarkBlue1
import com.example.weatherapp.ui.theme.DarkBlue2
import com.example.weatherapp.ui.theme.White
import com.example.weatherapp.ui.theme.Yellow
import com.example.weatherapp.viewmodel.HomeScreenViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bumptech.glide.Glide
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.rememberGlidePreloadingData
import com.example.weatherapp.data.models.CurrentResponseApi
import com.example.weatherapp.data.models.ResponseState
import com.example.weatherapp.utils.location.hasLocationPermission
import com.example.weatherapp.utils.timeZoneConversion
import com.example.weatherapp.utils.timeZoneConversionToHourly

//
//@Preview(showBackground = true)
//@Composable
//fun HomeScreenPreview() {
//    HomeScreen()
//}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(viewModel: HomeScreenViewModel) {

    val c =LocalContext.current
    RequestLocationPermission(
        onPermissionGranted = {
            viewModel.fetchLocation()
        },
        onPermissionDenied = {
            Toast.makeText(c, "Location permission denied!", Toast.LENGTH_LONG).show()
        }
    )



    var currentScreen by remember { mutableStateOf<ScreenRoute>(ScreenRoute.Home) }
    val uiState by viewModel.currentWeatherData.collectAsStateWithLifecycle()

    val hourlyForecast by viewModel.hourlyWeatherData.collectAsStateWithLifecycle()
    val dailyForecast by viewModel.dailyWeatherData.collectAsStateWithLifecycle()


    val message by viewModel.mutableMessage.collectAsStateWithLifecycle("")
    var weather by remember { mutableStateOf<CurrentResponseApi?>(null) }
    var hourly by remember { mutableStateOf<List<ForecastItem>?>(null) }
    var daily by remember { mutableStateOf<List<ForecastItem>?>(null) }


    val location by viewModel.currentLocation.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchLocation()

    }

    location?.let { loc ->
        Log.i("TAG", "HomeScreen:  ${loc.latitude}, Lon: ${loc.longitude}")
        Text("Lat: ${loc.latitude}, Lon: ${loc.longitude}")
    }

    when (uiState) {

        is ResponseState.Loading -> {
            LoadingIndicator()
        }

        is ResponseState.Success -> {
            weather = (uiState as ResponseState.Success).data
            hourly = when (hourlyForecast) {
                is ResponseState.Success -> (hourlyForecast as ResponseState.Success<List<ForecastItem>>).data
                else -> emptyList()
            }

            daily = when (dailyForecast) {
                is ResponseState.Success -> (dailyForecast as ResponseState.Success<List<ForecastItem>>).data
                else -> emptyList()
            }
            Log.d("TAG", "HomeScreen: ${daily}")
            Log.d("TAG", "HomeScreen: ${hourly}")
        }

        is ResponseState.Failure -> {
            Text(
                text = "Sorry, something went wrong can not load data",
                fontSize = 24.sp,
                color = White,
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize(),
            )
        }
    }


    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = BabyBlue,
        bottomBar = {
            BottomNavigationBar(
                currentScreen = currentScreen,
                onItemClick = { screen -> currentScreen = screen }
            )
        },

        ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxHeight()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            weather?.let { weatherData ->

                // Top Section
                item {
                    TopSection(weatherData)
                }

                // Weather Details
                item {
                    WeatherDetails(weatherData)
                }


                // Hourly Forecast
                item {
                    HourlyForecast(hourly)
                }
                // Daily Forecast
                item {
                    DailyForecast(daily)
                }
            }
        }
    }
}

@Composable
fun LoadingIndicator() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(White)
            .wrapContentSize(Alignment.Center)
            .background(White),


        ) {
        CircularProgressIndicator()
    }
}


@Composable
fun BottomNavigationBar(
    currentScreen: ScreenRoute,
    onItemClick: (ScreenRoute) -> Unit
) {
    val items = listOf(
        ScreenRoute.Home,
        ScreenRoute.Favorites,
        ScreenRoute.Alerts,
        ScreenRoute.Settings
    )

    NavigationBar(
        modifier = Modifier
            .background(White)
            .shadow(4.dp)
    ) {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                selected = currentScreen == item,
                onClick = { onItemClick(item) },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        modifier = Modifier.size(24.dp),
                        contentDescription = item.title
                    )
                },
                label = {
                    Text(
                        text = item.title
                    )
                }

            )
        }
    }

}

@Composable
fun DailyForecast(daily: List<ForecastItem>?) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 0.dp) // Keeps it aligned with other UI elements
    ) {
        Text(
            text = "Next 5 Days",
            fontWeight = FontWeight.ExtraBold,
            color = White,
            fontSize = 22.sp,
        )
        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            daily?.let {
                items(it.size) { idx ->
                    val forecast = it[idx]
                    DailyForecastItem(
                        date = timeZoneConversionToHourly(forecast.timestamp, "dd/MM\t\tEEEE"),
                        icon = "https://openweathermap.org/img/wn/${forecast.weather[0].icon}@4x.png",
                        temp = "${forecast.main.temp}",
                        measurement = "K"
                    )
                }
            }
        }
    }
}


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun DailyForecastItem(date: String, icon: String, temp: String, measurement: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .shadow(4.dp, shape = RoundedCornerShape(8.dp))
            .clip(RoundedCornerShape(8.dp)),
        shape = RoundedCornerShape(8.dp),
    ) {
        Row(
            modifier = Modifier
                .background(White)
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp), // Reduced padding
            verticalAlignment = Alignment.CenterVertically, // Align items vertically
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Date
            Text(
                text = date,
                fontSize = 14.sp,
                color = DarkBlue2
            )

            // Icon (Ensure it stays centered)
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .shadow(8.dp, shape = CircleShape) // Apply shadow
                    .clip(CircleShape)
                    .background(Color.Gray.copy(alpha = 0.3f)),
                contentAlignment = Alignment.Center,
            ) {
                GlideImage(
                    contentDescription = "Weather Icon",
                    modifier = Modifier.size(40.dp),
                    model = icon,
                )
            }

            // Temperature and Measurement
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = temp,
                    color = DarkBlue1,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                )
                Text(
                    text = measurement,
                    color = DarkBlue1,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 12.sp,
                )
            }
        }
    }
}


@Composable
fun HourlyForecast(hourly: List<ForecastItem>?) {

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = "Hourly Details",
            fontWeight = FontWeight.ExtraBold,
             color = White,
            fontSize = 22.sp,
        )
        Spacer(modifier = Modifier.height(8.dp))

        LazyRow(

            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp), // Ensure it has a defined width
            horizontalArrangement = Arrangement.spacedBy(8.dp),

            ) {
            hourly?.let {

                items(it.size) { idx ->
                    val forecast = it[idx]
                    HourlyForecastItem(
                        time = timeZoneConversionToHourly(forecast.timestamp),
                        icon = "https://openweathermap.org/img/wn/${forecast.weather[0].icon}@2x.png", // Avoid index errors
                        temp = "${forecast.main.temp}",
                        measurement = "K"
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun HourlyForecastItem(time: String, icon: String, temp: String, measurement: String = " K") {
    Card(
        modifier = Modifier
            .width(80.dp)
            .height(100.dp)
            .shadow(4.dp, shape = RoundedCornerShape(8.dp))
            .clip(RoundedCornerShape(8.dp))
            .wrapContentSize(Alignment.Center, true),
        shape = RoundedCornerShape(8.dp),

        ) {
        Column(
            modifier = Modifier
                .background(White)
                .width(80.dp)
                .height(120.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {

            Text(
                text = time,
                fontSize = 14.sp,
                color = DarkBlue2,
                modifier = Modifier
            )
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .shadow(8.dp, shape = CircleShape) // Apply shadow
                    .clip(CircleShape)
                    .background(Color.Yellow.copy(alpha = 0.4f)),

                contentAlignment = Alignment.Center,
            ) {

                GlideImage(
                    contentDescription = "Weather Icon",
                    modifier = Modifier
                        .size(40.dp),
                    model = icon,
                )
            }


            Row {
                Text(
                    text = temp,
                    fontSize = 12.sp,
                    color = DarkBlue2,
                    fontWeight = FontWeight.SemiBold,
                )
                Text(
                    text = measurement,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = DarkBlue2,
                )
            }
        }
    }
}

@Composable
fun TopSection(weather: CurrentResponseApi) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(BabyBlue),

        horizontalAlignment = Alignment.CenterHorizontally,

        ) {
        //location
        Text(
            text = "${weather.name}",
            fontWeight = FontWeight.Bold,
            fontSize = 32.sp,
            color = White
        )

        val date = weather.dt?.let { timeZoneConversion(it) }
        //data and time
        Text(
            text = "$date",
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = Yellow
        )


        Row {
            Text(
                text = "${weather.main?.temp}",
                fontWeight = FontWeight.Bold,
                fontSize = 64.sp,
                color = White
            )
            //temp type K, C, F

            Text(
                text = " K",
                fontWeight = FontWeight.Bold,
                fontSize = 38.sp,
                color = White
            )
        }


        /*   //icon
           //"https://openweathermap.org/img/wn/${weatherData.weather[0].icon}@4x.png"
           GlideImage(
               contentDescription = "Weather Icon",
               modifier = Modifier
                   .size(120.dp),
               model = "https://openweathermap.org/img/wn/${weatherData.weather[0].icon}@4x.png",
           )*/

        Text(
            text = "${weather.weather?.get(0)?.description}",
            fontWeight = FontWeight.SemiBold,
            color = Yellow,
            fontSize = 16.sp,

            )
        Text(
            text = "Clouds: ${weather.clouds?.all}%",
            fontWeight = FontWeight.SemiBold,
            color = Yellow,
            fontSize = 16.sp,

            )

    }
}


@Composable
fun WeatherDetails(weather: CurrentResponseApi) {
    Column(
        modifier = Modifier
            .padding(0.dp)
    ) {

        Text(
            text = "Weather Details",
            fontWeight = FontWeight.ExtraBold,
            color = White,
            fontSize = 22.sp,

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

        DetailedWeatherItem(
            label = "Pressure",
            value = "${weather.main?.pressure}",
            " hPa",
            icon = R.drawable.barometer
        )
        DetailedWeatherItem(
            label = "Humidity",
            value = "${weather.main?.humidity}",
            " %",
            icon = R.drawable.water_drop
        )
        DetailedWeatherItem(
            label = "Wind",
            value = "${weather.wind?.deg}",
            " Km/h",
            icon = R.drawable.wind
        )

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

@Composable
fun RequestLocationPermission(
    onPermissionGranted: () -> Unit,
    onPermissionDenied: () -> Unit
) {
    val context = LocalContext.current
    val activity = context as? Activity

    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            onPermissionGranted()
        } else {
            onPermissionDenied()
        }
    }

    LaunchedEffect(Unit) {
        if (!context.hasLocationPermission())
        {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        } else {
            onPermissionGranted()
        }
    }
}
