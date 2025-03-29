package com.example.weatherapp.utils.location

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationManager
import android.os.Looper
import android.util.Log
import com.google.android.gms.location.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch

class DefaultLocationClient(
    private val context: Context,
    private val fusedLocationProviderClient: FusedLocationProviderClient
) : LocationClient {

    @SuppressLint("MissingPermission")
    override suspend fun getCurrentLocation(): Flow<Location> {
        return callbackFlow {
            if (!context.hasLocationPermission()) {
                 throw LocationClient.LocationException("Missing location permission")

            }

            val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
            val isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

            if (!isGpsEnabled && !isNetworkEnabled) {
                throw LocationClient.LocationException("GPS is disabled")

            }

            val request = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 1000L)
                .setWaitForAccurateLocation(true)
                .build()

            val locationCallback = object : LocationCallback() {
                override fun onLocationResult(result: LocationResult) {
                    super.onLocationResult(result)
                    result.lastLocation?.let { location ->
                        Log.i("TAG", "Location received: ${location.latitude}, ${location.longitude}")
                       launch {  trySend(location)   }
                    }
                }
            }

            fusedLocationProviderClient.requestLocationUpdates(
                request,
                locationCallback,
                Looper.getMainLooper()
            )

            awaitClose {
                fusedLocationProviderClient.removeLocationUpdates(locationCallback)
            }
        }
    }
}
