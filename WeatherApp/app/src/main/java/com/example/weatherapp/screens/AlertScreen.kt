package com.example.weatherapp.screens

import android.app.TimePickerDialog
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.weatherapp.data.models.WeatherAlert
import com.example.weatherapp.ui.theme.BabyBlue
import com.example.weatherapp.ui.theme.DarkBlue2
import com.example.weatherapp.ui.theme.Yellow
import com.example.weatherapp.ui.theme.component.SwipeToDeleteContainer
import com.example.weatherapp.ui.theme.component.TopAppBar
import com.example.weatherapp.utils.SharedObject
import com.example.weatherapp.utils.location.DefaultLocationClient
import com.example.weatherapp.utils.location.LocationClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit


@Composable
fun Alert(
    viewModel: AlertViewModel
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val locationClient: LocationClient =
        DefaultLocationClient(context, LocationServices.getFusedLocationProviderClient(context))

    val lat = remember { mutableStateOf(0.0) }
    val lon = remember { mutableStateOf(0.0) }
    val snackBarHostState = remember { SnackbarHostState() }
    val calendar = Calendar.getInstance()
    val alertList by viewModel.alertFlow.collectAsStateWithLifecycle()

    val startTime = remember { mutableStateOf<Long>(0) }
    val endTime = remember { mutableStateOf<Long>(0) }
    val showAlertDialog = remember { mutableStateOf(false) }
    val showStartTimePicker = remember { mutableStateOf(false) }
    val showEndTimePicker = remember { mutableStateOf(false) }


    LaunchedEffect(Unit) {
        scope.launch(Dispatchers.IO) {
            locationClient.getCurrentLocation()
                .collect { location ->
                    Log.w(
                        "TAG",
                        "RequestLocationPermission= Home Location: ${location.latitude}, ${location.longitude}"
                    )
                    if (SharedObject.getString("loc", "GPS") == "Map") {
                        lat.value = SharedObject.getString("lat", "0.0").toDouble()
                        lon.value = SharedObject.getString("lon", "0.0").toDouble()

                    } else {
                        lat.value = location.latitude
                        lon.value = location.longitude
                    }
                }
        }
        viewModel.getAllAlerts()

    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackBarHostState) },
        containerColor = BabyBlue,
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 32.dp)
            .background(BabyBlue),
        topBar = { TopAppBar("Alert") },

        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    scope.launch {
                        snackBarHostState.showSnackbar("fab clicked")
                    }
                    showAlertDialog.value = true
                },
                shape = CircleShape,
                containerColor = DarkBlue2,
                contentColor = Yellow,
                modifier = Modifier
                    .padding(bottom = 24.dp),
            ) {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = "add Alert"
                )
            }
        },
    ) { pad ->


        if (alertList.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier
                    .padding(pad)
                    .navigationBarsPadding()
            ) {
                items(
                    count = alertList.size,
                    key = { it }
                ) { idx ->
                    val alert = alertList[idx]

                    SwipeToDeleteContainer(
                        item = alert,
                        onDelete = {

                            var deletedAlert = alert
                            viewModel.deleteAlert(alert)

                            scope.launch {
                                viewModel.getAllAlerts()
                                val result = snackBarHostState.showSnackbar(
                                    message = "Deleted",
                                    actionLabel = "Undo",
                                    duration = SnackbarDuration.Short
                                )

                                if (result == SnackbarResult.ActionPerformed) {
                                    viewModel.addAlert(
                                        deletedAlert
                                    )
                                }
                            }
                        }
                    ) {

                        AlertItemCard(alert)
                    }
                }
            }
        } else {
            Log.i("TAG", "Favourite: no doaa")
        }


    }


    if (showAlertDialog.value) {
        AlertDialog(
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp),
            onDismissRequest = { showAlertDialog.value = false },
            title = { Text("Select Alert Duration") },
            text = {
                Column {

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start

                    ) {

                        Button(onClick = { showStartTimePicker.value = true }) {
                            Text("Start Time")
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        val now = System.currentTimeMillis()
                        if (startTime.value < now) {
                            Text(
                                "Past Time!",
                                fontSize = 16.sp,
                                color = Color.Red,
                                fontWeight = FontWeight.Bold
                            )
                        } else {
                            Text(
                                "Start: ${formatTime(startTime.value)}",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Button(onClick = { showEndTimePicker.value = true }) {
                            Text("End Time")
                        }
                        Spacer(modifier = Modifier.width(8.dp))

                        if (endTime.value < startTime.value) {
                            Text(
                                "End time must be after start time",
                                fontSize = 16.sp,
                                color = Color.Red,
                                fontWeight = FontWeight.Bold
                            )
                        } else {
                            Text(
                                "End : ${formatTime(endTime.value)}",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                    }
                }
            },

            confirmButton = {
                Button(onClick = {

                    Log.i("TAG", "Alert: on done click")
                    val addedAlert = WeatherAlert(
                        startDate = startTime.value,
                        endDate = endTime.value,
                        lat = lat.value,
                        lon = lon.value,
                        cityName = ""
                    )
                    viewModel.addAlert(
                        addedAlert
                    )
                    showAlertDialog.value = false

                    viewModel.onTimeSelected(
                        context, startTime.value, addedAlert
                    )


                }) {
                    Text("Done")
                }
            }

        )

    }

    if (showStartTimePicker.value) {
        TimePickerDialog(
            context,
            { _, hour, minute ->
                calendar.set(Calendar.HOUR_OF_DAY, hour)
                calendar.set(Calendar.MINUTE, minute)
                calendar.set(Calendar.SECOND, 0)
                calendar.set(Calendar.MILLISECOND, 0)
                startTime.value = calendar.timeInMillis
                showStartTimePicker.value = false
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true
        ).show()
    }

    if (showEndTimePicker.value) {
        TimePickerDialog(
            context,
            { _, hour, minute ->
                calendar.set(Calendar.HOUR_OF_DAY, hour)
                calendar.set(Calendar.MINUTE, minute)
                calendar.set(Calendar.SECOND, 0)
                calendar.set(Calendar.MILLISECOND, 0)
                endTime.value = calendar.timeInMillis
                showEndTimePicker.value = false
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true
        ).show()
    }


}

fun formatTime(timestamp: Long): String {
    val sdf = SimpleDateFormat("hh:mm a", Locale.getDefault())
    return sdf.format(Date(timestamp))
}

@Composable
fun AlertItemCard(alert: WeatherAlert) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {
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
                imageVector = Icons.Default.Notifications,
                contentDescription = "Location Icon",
                tint = DarkBlue2,
                modifier = Modifier.size(28.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))


            Text(
                text = "Start: ${formatTime(alert.startDate)}  End: ${formatTime(alert.endDate)}",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = DarkBlue2,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = alert.cityName,
                fontSize = 18.sp,
                color = DarkBlue2,
                modifier = Modifier.weight(1f)
            )


        }
    }
}

fun showTimePicker(
    context: Context,
    showPicker: MutableState<Boolean>,
    selectedTime: MutableState<Long>,
    isStartTime: Boolean
) {
    if (showPicker.value) {
        TimePickerDialog(
            context,
            { _, hour, minute ->
                val now = Calendar.getInstance()
                val pickedTime = Calendar.getInstance().apply {
                    set(Calendar.HOUR_OF_DAY, hour)
                    set(Calendar.MINUTE, minute)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                }

                if (isStartTime) {
                    if (pickedTime.timeInMillis < now.timeInMillis) {
                        // Close picker, then show toast
                        showPicker.value = false
                        Handler(Looper.getMainLooper()).postDelayed({
                            Toast.makeText(
                                context,
                                "Start time cannot be in the past.",
                                Toast.LENGTH_SHORT
                            ).show()
                            showPicker.value = true
                        }, 500)
                    } else {
                        selectedTime.value = pickedTime.timeInMillis
                        showPicker.value = false
                    }
                } else {
                    if (pickedTime.timeInMillis < selectedTime.value) {
                        showPicker.value = false
                        Handler(Looper.getMainLooper()).postDelayed({
                            Toast.makeText(
                                context,
                                "End time must be after start time.",
                                Toast.LENGTH_SHORT
                            ).show()
                            showPicker.value = true
                        }, 500)
                    } else {
                        selectedTime.value = pickedTime.timeInMillis
                        showPicker.value = false
                    }
                }
            },
            Calendar.getInstance().get(Calendar.HOUR_OF_DAY),
            Calendar.getInstance().get(Calendar.MINUTE),
            true
        ).show()
    }
}
