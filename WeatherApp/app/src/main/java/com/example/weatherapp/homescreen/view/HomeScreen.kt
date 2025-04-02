package com.example.weatherapp.homescreen.view

import com.example.weatherapp.data.models.ForecastItem
import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import android.util.Log
import android.widget.Toast
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivities
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.weatherapp.data.models.CurrentResponseApi
import com.example.weatherapp.data.models.HomeEntity
import com.example.weatherapp.data.models.ResponseState
import com.example.weatherapp.homescreen.view.uicomponent.DailyForecast
import com.example.weatherapp.homescreen.view.uicomponent.Failure
import com.example.weatherapp.homescreen.view.uicomponent.HourlyForecast
import com.example.weatherapp.homescreen.view.uicomponent.WeatherDetails
import com.example.weatherapp.homescreen.viewmodel.HomeScreenViewModel
import com.example.weatherapp.ui.theme.BabyBlue
import com.example.weatherapp.ui.theme.White
import com.example.weatherapp.ui.theme.Yellow
import com.example.weatherapp.ui.theme.component.LoadingIndicator
import com.example.weatherapp.ui.theme.component.TopAppBar
import com.example.weatherapp.utils.SharedObject
import com.example.weatherapp.utils.formatNumberBasedOnLanguage
import com.example.weatherapp.utils.getSettingType
import com.example.weatherapp.utils.getTempUnit
import com.example.weatherapp.utils.getUnitSymbol
import com.example.weatherapp.utils.location.DefaultLocationClient
import com.example.weatherapp.utils.location.LocationClient
import com.example.weatherapp.utils.location.hasLocationPermission
import com.example.weatherapp.utils.timeZoneConversion
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(viewModel: HomeScreenViewModel) {

    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val uiState by viewModel.currentWeatherData.collectAsStateWithLifecycle()
    val hourlyForecast by viewModel.hourlyWeatherData.collectAsStateWithLifecycle()
    val dailyForecast by viewModel.dailyWeatherData.collectAsStateWithLifecycle()


    val lang by viewModel.lang
    val units by viewModel.unit

    val lat by viewModel.mapLat
    val lon by viewModel.mapLon

    val locationClient: LocationClient =
        DefaultLocationClient(context, LocationServices.getFusedLocationProviderClient(context))
    var weather by remember { mutableStateOf<CurrentResponseApi?>(null) }
    var hourly by remember { mutableStateOf<List<ForecastItem>?>(null) }
    var daily by remember { mutableStateOf<List<ForecastItem>?>(null) }
    var  offlineData  by remember { mutableStateOf<HomeEntity?>(null) }
    var unitTemp by remember { mutableStateOf("") }
    var unitSpeed by remember { mutableStateOf("") }

    LaunchedEffect(lang) {
      viewModel.updateDatabase()
        offlineData = viewModel.getFromDataBase()
        Log.w("home", "HomeScreen: ${offlineData?.id}", )
        Log.w("home", "HomeScreen: ${offlineData?.currentResponseApi}", )
        Log.w("home", "HomeScreen: ${offlineData?.forecastItem}", )
        val storedTemp = SharedObject.getString("temp", "Kelvin")
        val storedSpeed = SharedObject.getString("speed", "Meter/Sec (m/sec)")

        val translatedTemp = getSettingType(lang, "temp", storedTemp)
        val translatedSpeed = getSettingType(lang, "speed", storedSpeed)

        unitTemp = getUnitSymbol(lang, "temp", translatedTemp)
        unitSpeed = getUnitSymbol(lang, "speed", translatedSpeed)
    }

    RequestLocationPermission(
        onPermissionGranted = {
            scope.launch {
                locationClient.getCurrentLocation()
                    .collect { location ->

                        if (SharedObject.getString("loc", "GPS") == "Map") {
                            viewModel.loadForecastWeather(lat, lon, lang, units)
                            viewModel.loadCurrentWeather(lat, lon, lang, units)
                        } else {
                            viewModel.loadForecastWeather(
                                location.latitude,
                                location.longitude,
                                lang,
                                units
                            )
                            viewModel.loadCurrentWeather(
                                location.latitude,
                                location.longitude,
                                lang,
                                units
                            )
                        }
                    }
            }
        },
        onPermissionDenied = {
            enableLocationService(context)
            Toast.makeText(context, "Location permission denied!", Toast.LENGTH_LONG).show()
        }
    )

    LaunchedEffect(lang) {
        try {
            locationClient.getCurrentLocation()
                .catch { e ->
                    Toast.makeText(context, "Turn On location Please", Toast.LENGTH_LONG).show()
                    enableLocationService(context)
                }
                .collect { location ->
                    Log.w("TAG", "LaunchedEffect Updated,, Language: $lang")
                    Log.w("TAG", "LaunchedEffect Updated,, UNITS: $units")
                    if (SharedObject.getString("loc", "GPS") == "Map") {
                        viewModel.loadForecastWeather(lat, lon, lang, units)
                        viewModel.loadCurrentWeather(lat, lon, lang, units)
                    } else {

                        viewModel.loadForecastWeather(
                            location.latitude,
                            location.longitude,
                            lang,
                            units
                        )
                        viewModel.loadCurrentWeather(
                            location.latitude,
                            location.longitude,
                            lang,
                            units
                        )

                    }
                }
        } catch (e: Exception) {
            Log.i("TAG", "HomeScreenEX: Can no get location")
        }
    }





    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 32.dp)
           /// .statusBarsPadding()
           // .navigationBarsPadding(),
      ,  containerColor = BabyBlue,
        topBar = { TopAppBar("Home") }

    ) { innerPadding ->

        when (uiState) {
            is ResponseState.Loading -> LoadingIndicator()
            is ResponseState.Success -> {
                weather = (uiState as ResponseState.Success).data
            }

            is ResponseState.Failure ->{
              weather = offlineData?.currentResponseApi
            }
            //Failure("Can not Reload Data")
        }
        when (hourlyForecast) {
            is ResponseState.Loading -> LoadingIndicator()
            is ResponseState.Success -> {
                Log.i("TAG", "HomeScreen: success")
                hourly = (hourlyForecast as ResponseState.Success).data
            }

            is ResponseState.Failure -> {
                hourly = offlineData?.forecastItem?.list
                Failure("Can not Reload Hourly Data")
            }
        }
        when (dailyForecast) {
            is ResponseState.Loading -> LoadingIndicator()

            is ResponseState.Success -> {
                Log.i("TAG", "HomeScreen: success")
                daily = (dailyForecast as ResponseState.Success).data
            }

            is ResponseState.Failure ->{

                daily = offlineData?.forecastItem?.list
            }
            //Failure("Can not Reload Daily  Data")
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxHeight()
                .padding(innerPadding),
            //   .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {

            weather?.let { weatherData ->
                // Top Section
                item {
                    Log.d("TAG", "HomeScreen: waether compose")
                    TopSection(weatherData, unitTemp)
                }

                // Weather Details
                item {
                    WeatherDetails(weatherData,  unitSpeed)
                }

                // Hourly Forecast
                item {
                    HourlyForecast(hourly, unitTemp)
                }
                // Daily Forecast
                item {
                    DailyForecast(daily, unitTemp)
                }
            }
        }
    }
}


@Composable
fun TopSection(weather: CurrentResponseApi, unitTemp: String) {
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
                text = formatNumberBasedOnLanguage(weather.main?.temp?.toInt().toString()),
                fontWeight = FontWeight.Bold,
                fontSize = 64.sp,
                color = White
            )

            Text(
                text = unitTemp,
                fontWeight = FontWeight.Bold,
                fontSize = 38.sp,
                color = White
            )
        }


        /*   //icon

            GlideImage(

                contentDescription = "Weather Icon",
                modifier = Modifier.fillMaxSize(),
                model = getWeatherIcon(weather.weather?.get(0)?.icon.toString()),
            )
 */
        Text(
            text = "${weather.weather?.get(0)?.description}",
            fontWeight = FontWeight.SemiBold,
            color = Yellow,
            fontSize = 16.sp,

            )
        Text(
            text = "Clouds: ${formatNumberBasedOnLanguage(weather.clouds?.all.toString())}%",
            fontWeight = FontWeight.SemiBold,
            color = Yellow,
            fontSize = 16.sp,

            )

    }
}


fun enableLocationService(context: Context) {
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
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
            //   enableLocationService(context)
        } else {
            onPermissionGranted()
        }
    }
}
