@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.weatherapp.setting.uicomponent

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.weatherapp.R
import com.example.weatherapp.ui.theme.BabyBlue
import com.example.weatherapp.ui.theme.DarkBlue2
import com.example.weatherapp.ui.theme.Gray
import com.example.weatherapp.ui.theme.White
import com.example.weatherapp.utils.SharedObject
import com.example.weatherapp.utils.getLocationType
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
    val locationOptions = listOf(stringResource(R.string.gps), stringResource(R.string.map))

    var selectedLoc by remember {
        mutableStateOf(
            getLocationType(
                SharedObject.getString("lang", "en"),
                SharedObject.getString("loc", context.getString(R.string.gps))
            )
        )
    }

    var showMapDialog by remember { mutableStateOf(false) }
    var selectedLocation by remember { mutableStateOf<LatLng?>(null) }

    Column(
        modifier = Modifier
            .selectableGroup()
    ) {

        MainHeader(title = stringResource(R.string.location), icon = R.drawable.add_location)

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
                            if (selectedLoc == context.getString(R.string.map)) {
                                showMapDialog = true
                            }
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
                stringResource(R.string.select_location), modifier = Modifier
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
                    title = stringResource(R.string.here)
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
                    text = stringResource(R.string.confirm_location)
                )
            }
        }
    }
}
