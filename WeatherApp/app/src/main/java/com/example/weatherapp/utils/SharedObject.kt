package com.example.weatherapp.utils

import android.content.Context
import android.content.SharedPreferences

object SharedObject {
    private const val PREF_NAME = "settingPref"
    private lateinit var sharedPreferences: SharedPreferences

    fun init(context: Context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun saveString(key: String, value: String) {
        sharedPreferences.edit().putString(key, value).apply()
    }

    fun getString(key: String, defaultValue: String): String {
        return sharedPreferences.getString(key, defaultValue) ?: defaultValue
    }

    fun saveDouble(key: String, value: Float) {
        sharedPreferences.edit().putFloat(key, value).apply()
    }

    fun getDouble(key: String, defaultValue: Float): Float {
        return sharedPreferences.getFloat(key, defaultValue) ?: defaultValue
    }

}