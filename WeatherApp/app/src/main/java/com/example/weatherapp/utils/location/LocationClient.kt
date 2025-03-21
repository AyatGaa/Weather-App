package com.example.weatherapp.utils.location

import android.location.Location
import kotlinx.coroutines.flow.Flow

interface LocationClient {
    fun getCurrentLocation() : Flow<Location>
    class LocationException(message:String):Exception()
}