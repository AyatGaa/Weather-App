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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.weatherapp.R
import com.example.weatherapp.ui.theme.DarkBlue1
import com.example.weatherapp.ui.theme.DarkBlue2
import com.example.weatherapp.ui.theme.White
import com.example.weatherapp.utils.formatNumberBasedOnLanguage
import com.example.weatherapp.utils.getTempUnit
import com.example.weatherapp.utils.getWeatherIcon
import com.example.weatherapp.utils.timeZoneConversion


@Composable
fun DailyForecast(daily: List<ForecastItem>?, unitTemp:String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 0.dp)

    ) {
        Text(
            text = stringResource(R.string.next_5_days),
            fontWeight = FontWeight.ExtraBold,
            color = White,
            fontSize = 22.sp,
        )
        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            daily?.let {

                items(it.size) { idx ->
                    val forecast = it[idx]
                    val i = getWeatherIcon(forecast.weather[0].icon.toString())
                    DailyForecastItem(
                        date = timeZoneConversion(forecast.timestamp.toInt(), "dd/MM\t\tEEEE"),
                        icon = i,
                        temp = "${forecast.main.temp?.toInt()}",
                        measurement = unitTemp
                    )
                }
            }
        }
    }
}


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun DailyForecastItem(date: String, icon: Int, temp: String, measurement: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .shadow(4.dp, shape = RoundedCornerShape(8.dp))
            .clip(RoundedCornerShape(8.dp)),
        shape = RoundedCornerShape(8.dp),
    ) {
        Row(
            modifier = Modifier
                .background(White)
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Date
            Text(
                text = date,
                fontSize = 14.sp,
                color = DarkBlue2
            )

            // Icon
            Box(
                modifier = Modifier
                    .size(100.dp),
                contentAlignment = Alignment.Center,
            ) {
                GlideImage(
                    contentDescription = "Weather Icon",
                    modifier = Modifier.size(100.dp),
                    model = icon,
                )
            }

            // Temperature and Measurement
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = formatNumberBasedOnLanguage(temp),
                    color = DarkBlue1,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                )
                Text(
                    text = measurement,
                    color = DarkBlue1,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 12.sp,
                )
            }
        }
    }
}