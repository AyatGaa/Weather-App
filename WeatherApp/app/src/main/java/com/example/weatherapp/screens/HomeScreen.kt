package com.example.weatherapp.screens

import ForecastItem
import android.Manifest
import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.currentCompositeKeyHash
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue

import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivities
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp.navigation.ScreenRoute
import com.example.weatherapp.ui.theme.BabyBlue
import com.example.weatherapp.ui.theme.White
import com.example.weatherapp.ui.theme.Yellow
import com.example.weatherapp.viewmodel.HomeScreenViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.weatherapp.MainActivity
import com.example.weatherapp.data.models.CurrentResponseApi
import com.example.weatherapp.data.models.ResponseState
import com.example.weatherapp.data.remote.RetrofitHelper
import com.example.weatherapp.data.remote.WeatherRemoteDataSourceImpl
import com.example.weatherapp.data.repository.WeatherRepositoryImpl
import com.example.weatherapp.navigation.BottomNavigationBar
import com.example.weatherapp.navigation.NavRoutes
import com.example.weatherapp.navigation.NavigationItem
import com.example.weatherapp.navigation.SetupNavHost
import com.example.weatherapp.navigation.navigationItems
import com.example.weatherapp.uicomponent.DailyForecast
import com.example.weatherapp.uicomponent.Failure
import com.example.weatherapp.uicomponent.HourlyForecast
import com.example.weatherapp.uicomponent.LoadingIndicator
import com.example.weatherapp.uicomponent.WeatherDetails
import com.example.weatherapp.utils.location.DefaultLocationClient
import com.example.weatherapp.utils.location.LocationClient
import com.example.weatherapp.utils.location.hasLocationPermission
import com.example.weatherapp.utils.timeZoneConversion
import com.example.weatherapp.viewmodel.CurrentWeatherFactory
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.retry
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable



@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(viewModel: HomeScreenViewModel, onNavigateTo:()->Unit) {

    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    var currentScreen by remember { mutableStateOf<ScreenRoute>(ScreenRoute.Home) }
    val uiState by viewModel.currentWeatherData.collectAsStateWithLifecycle()
    val hourlyForecast by viewModel.hourlyWeatherData.collectAsStateWithLifecycle()
    val dailyForecast by viewModel.dailyWeatherData.collectAsStateWithLifecycle()

    val locationClient: LocationClient =
        DefaultLocationClient(context, LocationServices.getFusedLocationProviderClient(context))



    RequestLocationPermission(
        onPermissionGranted = {
            scope.launch {
                locationClient.getCurrentLocation()
                    .retry { e ->
                        if (e is LocationClient.LocationException) {
                            enableLocationService(context)
                            delay(2000)
                            return@retry true // gammed
                        }
                        false
                    }

                    .catch { e ->
                        Log.e("TAG", "Error Fetching Location: ${e.message}")

                    }.collect { location ->
                        Log.w(
                            "TAG",
                            "Home Location Fetched: ${location.latitude}, ${location.longitude}"
                        )
                        viewModel.loadForecastWeather(location.latitude, location.longitude, "en")
                        viewModel.loadCurrentWeather(location.latitude, location.longitude, "en")
                    }
            }
        },
        onPermissionDenied = {
            enableLocationService(context)
            Toast.makeText(context, "Location permission denied!", Toast.LENGTH_LONG).show()
        }
    )


    val message by viewModel.mutableMessage.collectAsStateWithLifecycle("")
    var weather by remember { mutableStateOf<CurrentResponseApi?>(null) }
    var hourly by remember { mutableStateOf<List<ForecastItem>?>(null) }
    var daily by remember { mutableStateOf<List<ForecastItem>?>(null) }

    val navController = rememberNavController()
    var selectedIndex by rememberSaveable { mutableIntStateOf(0) }


    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = BabyBlue,
        bottomBar = {
            BottomNavigationBar(navController )
        }

        ) { innerPadding ->

        when (uiState) {
            is ResponseState.Loading -> LoadingIndicator()

            is ResponseState.Success -> {
                weather = (uiState as ResponseState.Success).data
            }


            is ResponseState.Failure -> Failure("Can not Reload Data")
        }
        when (hourlyForecast) {

            is ResponseState.Loading -> LoadingIndicator()

            is ResponseState.Success -> {
                Log.i("TAG", "HomeScreen: success")
                hourly = (hourlyForecast as ResponseState.Success).data
            }

            is ResponseState.Failure -> {
                Failure("Can not Reload Hourly Data")
            }
        }
        when (dailyForecast) {
            is ResponseState.Loading -> LoadingIndicator()

            is ResponseState.Success -> {
                Log.i("TAG", "HomeScreen: success")
                daily = (dailyForecast as ResponseState.Success).data
            }

            is ResponseState.Failure -> Failure("Can not Reload Daily  Data")
        }
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
                    Log.d("TAG", "HomeScreen: waether compose")
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


fun enableLocationService(context: Context) {
    Toast.makeText(context, "Turn on location", Toast.LENGTH_LONG).show()
    val intent = arrayOf(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
    startActivities(context, intent)
}

@Composable
fun RequestLocationPermission(
    onPermissionGranted: () -> Unit,
    onPermissionDenied: () -> Unit
) {
    val context = LocalContext.current

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
        if (!context.hasLocationPermission()) {
            //enableLocationService(context)
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        } else {
            onPermissionGranted()
        }
    }
}
