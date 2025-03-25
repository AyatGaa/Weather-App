package com.example.weatherapp.utils

import com.example.weatherapp.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

//"dd MMMM, EEEE\n\t\t\t\t  hh:mm a"
fun timeZoneConversion(unixTime: Int, pattern: String = "dd MMMM, EEEE"): String {
    val date = Date(unixTime.toLong() * 1000)
    val formatter = SimpleDateFormat(pattern, Locale.getDefault())
    return formatter.format(date)
}


fun getTempUnit(): String {
    val unit = SharedObject.getString("temp", "Kelvin")
    var measure = " K"
    when (unit) {
        "Celsius" -> measure = " C"
        "Kelvin" -> measure = " K"
        "Fahrenheit" -> measure = " F"
    }
    return measure
}

fun getSpeedUnit(): String {
    val unit = SharedObject.getString("temp", "Kelvin")
    var measure = " K"
    when (unit) {
        "Celsius" -> measure = " m/s"
        "Kelvin" -> measure = " m/s"
        "Fahrenheit" -> measure = " mph"
    }
    return measure
}

fun getWeatherIcon(iconCode: String): Int {
    return when (iconCode) {
        "01d" -> R.drawable.day
        "01n" -> R.drawable.night
        "02d" -> R.drawable.cloudy_day_1
        "02n" -> R.drawable.cloudy_night_1
        "03d", "03n" -> R.drawable.cloudy
        "04d", "04n" -> R.drawable.cloudy
        "09d" -> R.drawable.rainy_3

        "09n" -> R.drawable.rainy_7
        "10d" -> R.drawable.rainy_1
        "10n" -> R.drawable.rainy_6
        "11d", "11n" -> R.drawable.thunder
        "13d" -> R.drawable.snowy_3

        "13n" -> R.drawable.snowy_6
        "50d", "50n" -> R.drawable.mist
        else -> R.drawable.weather // A fallback icon
    }
}

