package com.example.weatherapp.favorite.view

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.weatherapp.data.models.CityLocation
import com.example.weatherapp.data.models.ResponseState
import com.example.weatherapp.favorite.viewModel.FavoriteScreenViewModel
import com.example.weatherapp.ui.theme.BabyBlue
import com.example.weatherapp.ui.theme.DarkBlue2
import com.example.weatherapp.ui.theme.component.LoadingIndicator
import com.example.weatherapp.ui.theme.component.TopAppBar
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.launch


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Favourite(viewModel: FavoriteScreenViewModel, latLong: LatLng, onFabMapClick: () -> Unit) {

    val snackBarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    var selectedLocation by remember { mutableStateOf<LatLng?>(null) }
    var localCities by remember { mutableStateOf<List<CityLocation>?>(null) }


    val localCitiesState by viewModel.localCityFlow.collectAsStateWithLifecycle()

    val cityData by viewModel.uiState.collectAsStateWithLifecycle()
    LaunchedEffect(latLong) {
        selectedLocation = latLong
        viewModel.getLocationData(latLong.latitude, latLong.longitude)

    }
    LaunchedEffect(Unit) {
        viewModel.getAllFavoriteLocationFromDataBase()
        localCities = localCitiesState
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackBarHostState) },
        modifier = Modifier
            .statusBarsPadding()
            .navigationBarsPadding(),
        topBar = { TopAppBar("Favorites") },

        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    scope.launch {
                        snackBarHostState.showSnackbar("fab clicked")
                    }
                    onFabMapClick()
                },
                shape = CircleShape,
                containerColor = BabyBlue,
                contentColor = DarkBlue2,
                modifier = Modifier
                    .padding(bottom = 24.dp),
            ) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = "addLocation"
                )
            }
        },
    ) { pad ->


        when (cityData) {
            is ResponseState.Loading -> LoadingIndicator()
            is ResponseState.Success -> {
                val data = (cityData as ResponseState.Success).data
                Log.w("TAG", "Favourite: ${data.toString()}")
//                FavoriteItem(data.cityData.name, data.lat, data.lon, data.currentWeather.main?.temp){
//                    viewModel.deleteFromFavorite(data)
//                }

            }

            is ResponseState.Failure -> {
            }
        }
        // Show all saved locations
        if (localCitiesState.isNotEmpty()) {
            LazyColumn {
                items(localCitiesState.size) { idx ->
                    val city = localCitiesState[idx]
                    FavoriteItem(
                        city.cityData.name,
                        city.lat,
                        city.lon,
                        city.currentWeather.main?.temp
                    ){
                        viewModel.deleteFromFavorite(city)
                        viewModel.getAllFavoriteLocationFromDataBase()
                    }
                }
            }
        } else {
            Text(text = "No saved locations available", modifier = Modifier.padding(16.dp))
        }

    }

}
@Composable
fun FavoriteItem(cityName: String?, lat: Double, lon: Double, temp: Double?, onDeleteClick:()->Unit) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "City: $cityName")
        Text(text = "Latitude: $lat, Longitude: $lon")
        Text(text = "Temperature: ${temp ?: "N/A"}°C")

        Button(
            onClick = { onDeleteClick() }
        ) {

            Text("del")
        }
    }
}

