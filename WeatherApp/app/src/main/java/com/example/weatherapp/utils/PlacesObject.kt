package com.example.weatherapp.utils

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.example.weatherapp.BuildConfig
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.net.PlacesClient

object PlacesObject {
    private lateinit var placesClient: PlacesClient

    fun placesInit(context: Context) {
        Log.i("Search", "init PlaceCtry:  ")

        Places.initialize(context,"AIzaSyASPTItqNeWifC1NO4nUdCwQ-fLHeg-lx4")
        placesClient = Places.createClient(context)
    }

    fun getPlacesClient():PlacesClient{
        Log.i("Search", "get placClient: ${placesClient}")

        return placesClient
    }

    // Create a new PlacesClient instance
}