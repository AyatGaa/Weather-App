package com.example.weatherapp.ui.theme.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weatherapp.ui.theme.DarkBlue2

@Composable
fun TopAppBar(title: String) {
    Box(
        modifier = Modifier
            .background(Color.Transparent)
            .padding(8.dp)
            .fillMaxWidth(), contentAlignment = Alignment.Center
    ) {

        Text(
            text = title,
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            color = DarkBlue2,
            letterSpacing = 1.sp,
        )

    }
}
