package com.example.weatherapp.utils

import android.content.Context
import android.util.Log
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.net.PlacesClient

object SearchObject {

    private lateinit var placesClient: PlacesClient

    fun searchInit(context: Context) {
        Log.i("Search", "init PlaceCtry:  ")

        Places.initialize(context,"AIzaSyASPTItqNeWifC1NO4nUdCwQ-fLHeg-lx4")
        placesClient = Places.createClient(context)
    }

    fun getSearchClient(): PlacesClient {
        Log.i("Search", "get placClient: ${placesClient}")

        return placesClient
    }

}