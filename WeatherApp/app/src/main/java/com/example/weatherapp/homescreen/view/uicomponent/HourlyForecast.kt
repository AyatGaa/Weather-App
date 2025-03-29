package com.example.weatherapp.homescreen.view.uicomponent

import com.example.weatherapp.data.models.ForecastItem
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.weatherapp.ui.theme.DarkBlue2
import com.example.weatherapp.ui.theme.White
import com.example.weatherapp.utils.getTempUnit
import com.example.weatherapp.utils.getWeatherIcon
import com.example.weatherapp.utils.timeZoneConversion


@Composable
fun HourlyForecast(hourly: List<ForecastItem>?) {

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = "Hourly Details",
            fontWeight = FontWeight.ExtraBold,
            color = White,
            fontSize = 22.sp,
        )
        Spacer(modifier = Modifier.height(8.dp))

        LazyRow(

            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),

            ) {

            hourly?.let {
                val measurement = getTempUnit()
                items(it.size) { idx ->
                    val forecast = it[idx]
                    val ic = getWeatherIcon(forecast.weather[0].icon.toString())
                    HourlyForecastItem(
                        time = timeZoneConversion(forecast.timestamp.toInt()," hh:mm a"),
                        icon = ic,
                        temp = "${forecast.main.temp?.toInt()}",
                        measurement = measurement
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun HourlyForecastItem(time: String, icon: Int, temp: String, measurement: String = " K") {
    Card(
        modifier = Modifier
            .width(80.dp)
            .height(100.dp)
            .shadow(4.dp, shape = RoundedCornerShape(8.dp))
            .clip(RoundedCornerShape(8.dp))
            .wrapContentSize(Alignment.Center, true),
        shape = RoundedCornerShape(8.dp),

        ) {
        Column(
            modifier = Modifier
                .background(White)
                .width(80.dp)
                .height(120.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {

            Text(
                text = time,
                fontSize = 14.sp,
                color = DarkBlue2,
                modifier = Modifier
            )
            Box(
                modifier = Modifier
                    .size(40.dp),
                contentAlignment = Alignment.Center,
            ) {

                GlideImage(
                    contentDescription = "Weather Icon",
                    modifier = Modifier
                        .size(40.dp),
                    model = icon,
                )
            }


            Row {
                Text(
                    text = temp,
                    fontSize = 12.sp,
                    color = DarkBlue2,
                    fontWeight = FontWeight.SemiBold,
                )
                Text(
                    text = measurement,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = DarkBlue2,
                )
            }
        }
    }
}
