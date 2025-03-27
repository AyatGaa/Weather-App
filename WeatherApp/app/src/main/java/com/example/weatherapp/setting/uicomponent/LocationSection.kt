@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.weatherapp.setting.uicomponent

import android.util.Log
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.systemGesturesPadding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonColors
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.AbsoluteAlignment
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.example.weatherapp.R
import com.example.weatherapp.navigation.ScreenRoute
import com.example.weatherapp.ui.theme.BabyBlue
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


@Composable
fun LocationSection() {
    val context = LocalContext.current
    val locationOptions = listOf("GPS", "Map")
    var selectedLoc by remember {
        mutableStateOf(SharedObject.getString("loc", "GPS"))
    }


    var showMapDialog by remember { mutableStateOf(false) }
    var selectedLocation by remember { mutableStateOf<LatLng?>(null) }

    Column(
        modifier = Modifier
            .selectableGroup()
    ) {

        MainHeader("Location", Icons.Default.LocationOn)

        locationOptions.forEach { text ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .padding(start = 24.dp)
                    .selectable(
                        selected = (selectedLoc == text),
                        onClick = {

                            selectedLoc = text
                            SharedObject.saveString("loc", selectedLoc)
                            if (selectedLoc == "Map") {
                                showMapDialog = true
                            }

                            Toast.makeText(
                                context,
                                "Location set to $selectedLoc",
                                Toast.LENGTH_SHORT
                            ).show()

                        },
                        role = Role.RadioButton
                    )
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically

            ) {

                RadioButton(
                    selected = (text == selectedLoc),
                    onClick = null,
                    colors = RadioButtonColors(
                        selectedColor = DarkBlue2,
                        unselectedColor = Gray,
                        disabledSelectedColor = DarkBlue2,
                        disabledUnselectedColor = Gray
                    ),
                )
                Text(
                    text = text,
                    modifier = Modifier.padding(start = 4.dp)
                )
            }

        }

    }

    if (showMapDialog) {
        MapDialog(
            onDismiss = { showMapDialog = false },
            onLocationSelected = { latLng ->
                selectedLocation = latLng
                SharedObject.saveString("lat", "${latLng.latitude}")
                SharedObject.saveString("lon", "${latLng.longitude}")
                showMapDialog = false //to close dialog
            }
        )
    }
}

@Composable
fun MapDialog(
    onDismiss: () -> Unit,
    onLocationSelected: (LatLng) -> Unit
) {
    var selectedLocation by remember {
        mutableStateOf(
            LatLng(
                SharedObject.getString("lat", "0").toDouble(),
                SharedObject.getString("lon", "0").toDouble()
            )
        )
    }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(selectedLocation, 15f)
    }

    Dialog(onDismissRequest = { onDismiss() }) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(BabyBlue)
        ) {

            Text(
                "Select Location", modifier = Modifier
                    .padding(16.dp)
            )

            GoogleMap(
                modifier = Modifier.weight(1f),
                cameraPositionState = cameraPositionState,
                onMapClick = { latLng ->
                    selectedLocation = latLng
                    Log.w("TAG", "MapDialog: need to save hrer?")
                }, properties = MapProperties(mapType = MapType.NORMAL)
            ) {
                Marker(
                    state = MarkerState(position = selectedLocation),
                    title = "Here"
                )
            }

            Button(
                colors = ButtonColors(
                    contentColor = White,
                    containerColor = DarkBlue2,
                    disabledContentColor = DarkBlue2,
                    disabledContainerColor = DarkBlue2
                ),
                onClick = {
                    onLocationSelected(selectedLocation)
                    onDismiss()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Confirm Location"
                )
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(
    onLocationSelected: (locationDetails: LatLng) -> Unit
) {
    val context = LocalContext.current
    var selectedLocation by remember { mutableStateOf<LatLng?>(null) } // Use nullable type
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
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 24.dp)
                .statusBarsPadding(),


            ) {
            TextField(
                value = " ",
                onValueChange = { it },
                placeholder = { Text("Search Location", color = Color.Gray) },
                textStyle = TextStyle(color = Color.Black),
                modifier =
                Modifier
                    .fillMaxWidth()
                    .background(Color.White, shape = RoundedCornerShape(12.dp))
                    .shadow(4.dp, RoundedCornerShape(12.dp))
                    .padding(8.dp),

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
        }

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

