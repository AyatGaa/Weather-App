package com.example.weatherapp.screens

import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
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
import com.example.weatherapp.favorite.FavoriteScreenViewModel
import com.example.weatherapp.ui.theme.BabyBlue
import com.example.weatherapp.ui.theme.DarkBlue2
import com.example.weatherapp.ui.theme.component.TopAppBar
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.launch


@Composable
fun Favourite(viewModel: FavoriteScreenViewModel, latLong: LatLng, onFabMapClick: () -> Unit) {

    val snackBarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    var selectedLocation by remember { mutableStateOf<LatLng?>(null) }

    LaunchedEffect(latLong) {
        selectedLocation = latLong
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
        Text(
            text = "Selected: ${selectedLocation?.latitude ?: "None"}, ${selectedLocation?.longitude ?: "None"}",
            modifier = Modifier.padding(pad)
        )


    }
}