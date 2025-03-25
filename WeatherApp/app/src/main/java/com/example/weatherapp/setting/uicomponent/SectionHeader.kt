package com.example.weatherapp.setting.uicomponent

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weatherapp.ui.theme.DarkBlue2
import com.example.weatherapp.ui.theme.Yellow


@Composable
fun SectionHeader(title: String) {
    Row(
        modifier = Modifier
            .padding(start = 16.dp, end = 16.dp)

            .clip(RoundedCornerShape(bottomEnd = 8.dp, bottomStart = 8.dp))
            .background(Yellow)
            .shadow(40.dp)
            .padding(8.dp)
            .fillMaxWidth()
            .wrapContentSize(Alignment.CenterStart)
    ) {
        Text(
            text = title,
            fontSize = 16.sp,
            color = DarkBlue2
        )

    }

}
