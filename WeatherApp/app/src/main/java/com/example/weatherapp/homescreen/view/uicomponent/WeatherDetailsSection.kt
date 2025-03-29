package com.example.weatherapp.homescreen.view.uicomponent

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weatherapp.R
import com.example.weatherapp.data.models.CurrentResponseApi
import com.example.weatherapp.ui.theme.DarkBlue1
import com.example.weatherapp.ui.theme.DarkBlue2
import com.example.weatherapp.ui.theme.White
import com.example.weatherapp.ui.theme.Yellow
import com.example.weatherapp.utils.getSpeedUnit
import com.example.weatherapp.utils.getWeatherIcon


@Composable
fun WeatherDetails(weather: CurrentResponseApi) {
    Column(
        modifier = Modifier
            .padding(0.dp)
    ) {

        Text(
            text = "Weather Details",
            fontWeight = FontWeight.ExtraBold,
            color = White,
            fontSize = 22.sp,

            )
    }
    Spacer(modifier = Modifier.height(8.dp))
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(
            16.dp,
            alignment = Alignment.CenterHorizontally
        )

    ) {
        val measure =getSpeedUnit()
        DetailedWeatherItem(
            label = "Pressure",
            value = "${weather.main?.pressure}",
            " hPa",
            icon = R.drawable.barometer
        )
        DetailedWeatherItem(
            label = "Humidity",
            value = "${weather.main?.humidity}",
            " %",
            icon = R.drawable.water_drop
        )
        DetailedWeatherItem(
            label = "Wind",
            value = "${weather.wind?.deg}",
           measurement =  measure,
            icon = R.drawable.wind
        )

    }

}


@Composable
fun DetailedWeatherItem(label: String, value: String, measurement: String, icon: Int) {
    Card(
        modifier = Modifier
            .width(100.dp)
            .height(120.dp)
            .shadow(4.dp, shape = RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .background(Yellow)
                .width(100.dp)
                .height(120.dp)
                .padding(0.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,

            ) {
            // Icon

            Icon(
                painter = painterResource(id = icon),
                contentDescription = label,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            // Label
            Text(
                text = label,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = DarkBlue2
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Value and Measurement
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = value,
                    color = DarkBlue1
                )
                Text(
                    text = measurement,
                    color = DarkBlue1
                )
            }
        }
    }
}
