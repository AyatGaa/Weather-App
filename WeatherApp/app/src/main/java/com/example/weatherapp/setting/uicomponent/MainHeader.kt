package com.example.weatherapp.setting.uicomponent

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weatherapp.ui.theme.DarkBlue1
import com.example.weatherapp.ui.theme.White


@Composable
fun MainHeader(title: String, icon: ImageVector?) {
    Row(
        modifier = Modifier

            .background(DarkBlue1)
            .padding(8.dp)
            .fillMaxWidth()
            .wrapContentSize(Alignment.CenterStart)
    ) {
        icon?.let {
            Icon(
                modifier = Modifier.size(18.dp),
                imageVector = it,
                tint = White,
                contentDescription = "location logo"
            )
        }
        Text(
            text = title,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = White
        )

    }

}
