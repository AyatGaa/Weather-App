package com.example.weatherapp.utils.location

import android.annotation.SuppressLint
import android.content.Context
import android.health.connect.datatypes.ExerciseRoute
import android.location.Location
import android.location.LocationManager
import android.os.Looper
import android.util.Log
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.Priority
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch

class DefaultLocationClient(

    private val context: Context,
    private val fusedLocationProviderClient: FusedLocationProviderClient
) : LocationClient {
    @SuppressLint("MissingPermission")
    override fun getCurrentLocation(): Flow<Location> {
        return callbackFlow {
            if (!context.hasLocationPermission()) {
                throw LocationClient.LocationException("Missing location permission")
            }
            val locationManager =
                context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
            val isNetworkEnabled =
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

            if (!isNetworkEnabled && !isGpsEnabled) {
                throw LocationClient.LocationException("GPS is disables")
            }
            //fetch location

            val request = LocationRequest.Builder(0)
                .apply {
                    setPriority(Priority.PRIORITY_HIGH_ACCURACY)
                }.build()

            val locationCallback = object : LocationCallback() {
                override fun onLocationResult(result: LocationResult) {
                    super.onLocationResult(result)

                    result.lastLocation?.let { location ->
                        Log.i("TAG", "onLocationResult: ${result.lastLocation.toString()}")
                      launch { send(location) }
                    }
                }
            }

            fusedLocationProviderClient.requestLocationUpdates(
                request,
                locationCallback,
                Looper.getMainLooper()
            )

            awaitClose { fusedLocationProviderClient.removeLocationUpdates(locationCallback) }
        }
    }

}