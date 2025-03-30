package com.example.weatherapp.screens

import android.app.TimePickerDialog
import android.util.Log
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.LocationOn
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.weatherapp.favorite.view.FavoriteItemCard
import com.example.weatherapp.ui.theme.BabyBlue
import com.example.weatherapp.ui.theme.DarkBlue2
import com.example.weatherapp.ui.theme.Yellow
import com.example.weatherapp.ui.theme.component.SwipeToDeleteContainer
import com.example.weatherapp.ui.theme.component.TopAppBar
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


@Composable
fun Alert(
    viewModel: AlertViewModel


) {

    val snackBarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    val alertList by viewModel.alertFlow.collectAsStateWithLifecycle()


    val startTime = remember { mutableStateOf<Long>(0) }
    val endTime = remember { mutableStateOf<Long>(0) }
    val showAlertDialog = remember { mutableStateOf(false) }
    val showStartTimePicker = remember { mutableStateOf(false) }
    val showEndTimePicker = remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
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
            LazyColumn(modifier = Modifier.padding(pad).navigationBarsPadding()) {
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
                                   viewModel.addAlert(deletedAlert.startDate,deletedAlert.endDate) // Restore if Undo is clicked
                                }
                            }
                        }
                    ) {

                        AlertItemCard(startTime.value,endTime.value)
                    }
                }
            }
        } else {
            Log.i("TAG", "Favourite: no doaa")
        }

//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(pad),
//            verticalArrangement = Arrangement.Center,
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            Text(text = "Start Time: ${startTime.value?.let { formatTime(it) }}")
//            Text(text = "End Time: ${endTime.value?.let { formatTime(it) }}")
//        }


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
                            Text("Pick Start Time")
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "Start: ${formatTime(startTime.value)}",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Button(onClick = { showEndTimePicker.value = true }) {
                            Text("Pick End Time")
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "End : ${formatTime(endTime.value)}",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            },

            confirmButton = {
                Button(onClick = {

                    Log.i("TAG", "Alert: on done click")
                    viewModel.addAlert(startTime.value, endTime.value)
                    showAlertDialog.value = false


                    // can be fun in view model to set up notification
                    viewModel.onTimeSelected(startTime.value, endTime.value)

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
fun AlertItemCard(start:Long, end:Long) {
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
                text = "Start: ${formatTime(start)} : End: ${formatTime(end)}",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = DarkBlue2,
                modifier = Modifier.weight(1f)
            )


            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = "View Details",
                tint = DarkBlue2,
                modifier = Modifier.size(32.dp)
            )
        }
    }
}

