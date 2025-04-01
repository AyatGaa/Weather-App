package com.example.weatherapp.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences

fun restartActivity(context: Context) {

    val intent = (context as? Activity)?.intent
    intent?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
    context.startActivity(intent)
    (context as? Activity)?.finish()

}

fun getLanguage(apiUnit: String): String {
    return when (apiUnit) {
        "English" -> "en"
        "العربية" -> "ar"
        else -> "en"
    }
}

fun getLanguageReverse(apiUnit: String): String {
    return when (apiUnit) {
        "en" -> "English"
        "ar" -> "العربية"
        else -> "English"
    }
}


fun getLocationType(lang: String, location: String): String {
    return when {
        lang == "en" && location == "نظام تحديد المواقع (GPS)" -> "GPS"
        lang == "ar" && location == "GPS" -> "نظام تحديد المواقع (GPS)"
        lang == "en" && location == "الخريطة" -> "Map"
        lang == "ar" && location == "Map" -> "الخريطة"
        else -> location
    }


}

fun getSettingType(lang: String, settingType: String, value: String): String {
    return when {
        lang == "en" && settingType == "temp" && value == "كلفن" -> "Kelvin"
        lang == "ar" && settingType == "temp" && value == "Kelvin" -> "كلفن"
        lang == "en" && settingType == "temp" && value == "درجة مئوية" -> "Celsius"
        lang == "ar" && settingType == "temp" && value == "Celsius" -> "درجة مئوية"
        lang == "en" && settingType == "temp" && value == "فهرنهايت" -> "Fahrenheit"
        lang == "ar" && settingType == "temp" && value == "Fahrenheit" -> "فهرنهايت"

        lang == "en" && settingType == "speed" && value == "متر/ثانية (م/ث)" -> "Meter/Sec (m/sec)"
        lang == "ar" && settingType == "speed" && value == "Meter/Sec (m/sec)" -> "متر/ثانية (م/ث)"
        lang == "en" && settingType == "speed" && value == "ميل/ساعة (ميل/س)" -> "Miles/Hour (mph)"
        lang == "ar" && settingType == "speed" && value == "Miles/Hour (mph)" -> "ميل/ساعة (ميل/س)"
        else -> value
    }
}
fun getUnitSymbol(lang: String, settingType: String , value: String): String {
    return when {
        lang == "en" && settingType == "temp" && value == "Kelvin" -> "K"
        lang == "ar" && settingType == "temp" && value == "كلفن" -> "ك"
        lang == "en" && settingType == "temp" && value == "Celsius" -> "°C"
        lang == "ar" && settingType == "temp" && value == "درجة مئوية" -> "°م"
        lang == "en" && settingType == "temp" && value == "Fahrenheit" -> "°F"
        lang == "ar" && settingType == "temp" && value == "فهرنهايت" -> "°ف"
        lang == "en" && settingType == "speed" && value == "Meter/Sec (m/sec)" -> "m/s"
        lang == "ar" && settingType == "speed" && value == "متر/ثانية (م/ث)" -> "م/ث"
        lang == "en" && settingType == "speed" && value == "Miles/Hour (mph)" -> "mph"
        lang == "ar" && settingType == "speed" && value == "ميل/ساعة (ميل/س)" -> "ميل/ساعة"

        else -> value
    }
}


