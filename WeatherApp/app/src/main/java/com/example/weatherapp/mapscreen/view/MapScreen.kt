package com.example.weatherapp.mapscreen.view

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.example.weatherapp.R
import com.example.weatherapp.mapscreen.viewModel.MapViewModel
import com.example.weatherapp.ui.theme.DarkBlue2
import com.example.weatherapp.ui.theme.Gray
import com.example.weatherapp.ui.theme.White
import com.example.weatherapp.ui.theme.Yellow
import com.example.weatherapp.utils.SharedObject
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(

    viewModel: MapViewModel,
    onLocationSelected: (LatLng) -> Unit
) {
    val searchResult by viewModel.searchResults.collectAsState(emptyList())


    var searchText by remember { mutableStateOf("") }
    var selectedLocation by remember { mutableStateOf<LatLng?>(null) }  //mutable of remem ber
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(
            LatLng(
                SharedObject.getString("lat", "0.0").toDouble(),
                SharedObject.getString("lon", "0.0").toDouble(),
            ), 8f
        )
    }
    Box(

        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding()

    ) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            properties = MapProperties(mapType = MapType.NORMAL),
            onMapClick = { latLng ->
                Log.w("TAG", "MapScreen: map clicked at ${latLng.latitude}, ${latLng.longitude}")
                selectedLocation = latLng
            }

        ) {
            selectedLocation?.let {
                Marker(
                    state = MarkerState(position = it),
                    title = "Selected Location"
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 24.dp)
                .statusBarsPadding()
        ) {
            // Search Bar
            TextField(
                value = searchText,
                onValueChange = { txt ->
                    searchText = txt
                    viewModel.onSearch(txt) },
                placeholder = { Text("Search Location", color = Color.Gray) },
                textStyle = TextStyle(color = Color.Black),
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, shape = RoundedCornerShape(12.dp))
                    .shadow(6.dp, shape = RoundedCornerShape(12.dp)),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search Icon",
                        tint = Color.Gray
                    )
                },
                colors = TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor = DarkBlue2,
                    unfocusedIndicatorColor = Gray
                ),
                singleLine = true
            )
            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                items(searchResult) { place ->
                    Text(
                        text = place.name, // ✅ Corrected
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                viewModel.onPlaceSelected(place.lat,place.lon) { latLng ->
                                    onLocationSelected(latLng)
                                }
                            }
                            .background(White)
                            .padding(12.dp)
                    )
                }
            }
        }
        // Results List



        Box(

            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 72.dp, end = 16.dp),
            contentAlignment = Alignment.BottomEnd

        ) {
            FloatingActionButton(

                onClick = {
                    selectedLocation?.let { location ->
                        Log.w(
                            "TAG",
                            "MapScreen: Confirm button clicked with ${location.latitude}, ${location.longitude}"
                        )
                        onLocationSelected(location)
                    }
                },
                shape = CircleShape,
                containerColor = DarkBlue2,
                contentColor = Yellow,

                ) {
                Icon(
                    painter = painterResource(R.drawable.add_location),
                    contentDescription = "addLocation"
                )
            }
        }

    }
}

