package com.example.weatherapp.favorite.view

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.weatherapp.data.models.CurrentResponseApi
import com.example.weatherapp.data.models.ResponseState
import com.example.weatherapp.favorite.viewModel.FavoriteScreenViewModel
import com.example.weatherapp.homescreen.view.uicomponent.DailyForecast
import com.example.weatherapp.homescreen.view.uicomponent.Failure
import com.example.weatherapp.homescreen.view.uicomponent.HourlyForecast
import com.example.weatherapp.homescreen.view.uicomponent.WeatherDetails
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
import com.example.weatherapp.utils.timeZoneConversion

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun FavoriteCardDetail(viewModel: FavoriteScreenViewModel, lat: Double, lon: Double, id: Int) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var lang by remember { mutableStateOf("") }

    var unitTemp by remember { mutableStateOf("") }
    var unitSpeed by remember { mutableStateOf("") }


    LaunchedEffect(lang) {
        lang = SharedObject.getString("lang", "en")

        val storedTemp = SharedObject.getString("temp", "Kelvin")
        val storedSpeed = SharedObject.getString("speed", "Meter/Sec (m/sec)")

        val translatedTemp = getSettingType(lang, "temp", storedTemp)
        val translatedSpeed = getSettingType(lang, "speed", storedSpeed)

        unitTemp = getUnitSymbol(lang, "temp", translatedTemp)
        unitSpeed = getUnitSymbol(lang, "speed", translatedSpeed)
    }

    LaunchedEffect(Unit) {
        viewModel.getLocationDetailsForCardOnline(lat, lon)
        viewModel.getLocationDetailsForCardOffline(lat, lon, id)
    }


    when (uiState) {
        is ResponseState.Loading -> LoadingIndicator()
        is ResponseState.Success -> {
            val data = (uiState as ResponseState.Success).data

            Scaffold(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .navigationBarsPadding(),
                containerColor = BabyBlue,
                topBar = { TopAppBar("Home") }
            ) { innerPadding ->

                LazyColumn(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(innerPadding)
                        .statusBarsPadding()
                        .navigationBarsPadding(),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {

                    // Top Section
                    item {
                        Log.d("TAG", "HomeScreen: waether compose")
                        TopSection(data.currentWeather,unitTemp)
                    }

                    // Weather Details
                    item {
                        WeatherDetails(data.currentWeather, unitSpeed)
                    }

                    // Hourly Forecast
                    item {
                        HourlyForecast(data.forecastWeather.list.take(8), unitTemp)
                    }
                    // Daily Forecast
                    item {
                        DailyForecast(data.forecastWeather.list.groupBy {
                            it.timestamp.let { ts ->
                                java.time.Instant.ofEpochSecond(ts)
                                    .atZone(java.time.ZoneOffset.UTC)
                                    .toLocalDate()
                            }
                        }.map { (_, forecasts) ->
                            forecasts.first()
                        }, unitTemp)
                    }
                }
            }


        }

        is ResponseState.Failure -> {
            viewModel.getLocationDetailsForCardOffline(lat, lon, id)
        }
    }

}


@Composable
fun TopSection(weather: CurrentResponseApi,unitTemp:String) {
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
            //temp type K, C, F
             Text(
                text = unitTemp,
                fontWeight = FontWeight.Bold,
                fontSize = 38.sp,
                color = White
            )
        }

        Text(
            text = "${weather.weather?.get(0)?.description}",
            fontWeight = FontWeight.SemiBold,
            color = Yellow,
            fontSize = 16.sp,

            )
        Text(
            text = "Clouds: ${ formatNumberBasedOnLanguage(weather.clouds?.all.toString())}%",
            fontWeight = FontWeight.SemiBold,
            color = Yellow,
            fontSize = 16.sp,

            )
    }
}


