package com.example.weatherapp.ui.theme.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.weatherapp.ui.theme.DarkBlue2
import com.example.weatherapp.ui.theme.White

@Composable
fun LoadingIndicator() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(White)
            .wrapContentSize(Alignment.Center)
            .background(White),
    ) {
        CircularProgressIndicator(color = DarkBlue2)
    }
}
