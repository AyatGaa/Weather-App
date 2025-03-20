package com.example.weatherapp.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
//"dd MMMM, EEEE\n\t\t\t\t  hh:mm a"
  fun timeZoneConversion( unixTime: Int, pattern: String = "dd MMMM, EEEE"): String {
    val date = Date(unixTime.toLong() * 1000) // Convert seconds to milliseconds
    val formatter = SimpleDateFormat(pattern, Locale.getDefault())
    return formatter.format(date)
}
  fun timeZoneConversionToHourly( unixTime: Long, pattern: String = " hh:mm a"): String {
    val date = Date(unixTime.toLong() * 1000) // Convert seconds to milliseconds
    val formatter = SimpleDateFormat(pattern, Locale.getDefault())
    return formatter.format(date)
}


// standard = Kelvin

// kelvin to celsius

fun kelvinToCelsius(kelvinTemp:Double ):Double{
  return (kelvinTemp - 273.15)
}

fun KelvinToFahrenheit(kelvinTemp:Double ):Double{
  return ((kelvinTemp - 273.15)*(5/9) + 32)
}