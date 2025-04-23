package com.example.weatherapp.favorite.view

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.weatherapp.R
import com.example.weatherapp.data.models.CityLocation
import com.example.weatherapp.data.models.ResponseState
import com.example.weatherapp.favorite.viewModel.FavoriteScreenViewModel
import com.example.weatherapp.ui.theme.BabyBlue
import com.example.weatherapp.ui.theme.DarkBlue2
import com.example.weatherapp.ui.theme.Yellow
import com.example.weatherapp.ui.theme.component.LoadingIndicator
import com.example.weatherapp.ui.theme.component.NoFavoritesIllustration
import com.example.weatherapp.ui.theme.component.SwipeToDeleteContainer
import com.example.weatherapp.ui.theme.component.TopAppBar
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.launch


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Favourite(
    viewModel: FavoriteScreenViewModel,
    latLong: LatLng,
    onFabMapClick: () -> Unit,
    onFavoriteCardClick: (CityLocation) -> Unit
) {

    val snackBarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    var selectedLocation by remember { mutableStateOf<LatLng?>(null) }
    var localCities by remember { mutableStateOf<List<CityLocation>?>(null) }
    var messeage =  viewModel.mutableDatabaseMessage.collectAsStateWithLifecycle("Snack")

    val localCitiesState by viewModel.localCityFlow.collectAsStateWithLifecycle()
    val cityData by viewModel.uiState.collectAsStateWithLifecycle()


    LaunchedEffect(latLong) {
        Log.w("fav", "Favourite: get data from map screen $latLong" )
        selectedLocation = latLong
        Log.w("fav", "Favourite: get data from map screen $selectedLocation" )
        viewModel.getLocationData(latLong.latitude, latLong.longitude)
        viewModel.getCurrentSetting()

    }
    LaunchedEffect(Unit) {
        viewModel.getAllFavoriteLocationFromDataBase()
        Log.d("fav", "Fetched cities: ${localCitiesState}")
        localCities = localCitiesState
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackBarHostState) },
        containerColor = BabyBlue,
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 32.dp)
            .background(BabyBlue),
        topBar = { TopAppBar(stringResource(R.string.favorites)) },

        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    scope.launch {
                        snackBarHostState.showSnackbar("fab clicked")
                    }
                    onFabMapClick()
                },
                shape = CircleShape,
                containerColor = DarkBlue2,
                contentColor = Yellow,
                modifier = Modifier
                    .padding(bottom = 24.dp),
            ) {
                Icon(
                    painter = painterResource(R.drawable.add_location),
                    contentDescription = "addLocation"
                )
            }
        },
    ) { pad ->

        when (cityData) {
            is ResponseState.Loading -> LoadingIndicator()
            is ResponseState.Success -> {
                val data = (cityData as ResponseState.Success).data

            }

            is ResponseState.Failure -> {

            }
        }
        if (localCitiesState.isNotEmpty()) {
            LazyColumn(modifier = Modifier
                .padding(pad)
                .navigationBarsPadding()) {
                items(
                    count = localCitiesState.size,
                    key = { it }
                ) { idx ->
                    val city = localCitiesState[idx]

                    SwipeToDeleteContainer(
                        item = city,
                        onDelete = {

                            var deletedCity = city
                            viewModel.deleteFromFavorite(city)

                            scope.launch {
                                viewModel.getAllFavoriteLocationFromDataBase()
                                val result = snackBarHostState.showSnackbar(
                                    message = "${messeage.value} ${ city.cityData.name}",
                                    actionLabel = "Undo",
                                    duration = SnackbarDuration.Short
                                )

                                if (result == SnackbarResult.ActionPerformed) {
                                    viewModel.addFavouriteLocation(deletedCity) // Restore if Undo is clicked
                                }
                            }
                        }
                    ) {

                        FavoriteItemCard(city) {
                            onFavoriteCardClick(city)
                        }
                    }
                }
            }
        } else {
            NoFavoritesIllustration()
        }
    }
}


@Composable
fun FavoriteItemCard(
    location: CityLocation,
    onClick: (CityLocation) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {
                onClick(location)
            },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.elevatedCardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Yellow)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = "Location Icon",
                tint = DarkBlue2,
                modifier = Modifier.size(28.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            location.currentWeather.name?.let { it1 ->
                Text(
                    text = it1,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkBlue2,
                    modifier = Modifier.weight(1f)
                )
            }


            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = "View Details",
                tint = DarkBlue2,
                modifier = Modifier.size(32.dp)
            )
        }
    }
}

