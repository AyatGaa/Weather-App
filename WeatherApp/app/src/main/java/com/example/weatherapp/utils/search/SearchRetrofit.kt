package com.example.weatherapp.utils.search

import com.example.weatherapp.utils.SharedObject
import com.google.gson.annotations.SerializedName
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface LocationApi {
    @GET("search")
    suspend fun getLocations(
        @Query("q") query: String,
        @Query("format") format: String = "json",
        @Query("accept-language") language: String = SharedObject.getString("lang", "en")
    ): List<LocationData>
}


data class LocationData(
    @SerializedName("display_name") val name: String,
    @SerializedName("lat") val lat: Double,
    @SerializedName("lon") val lon: Double,
    @SerializedName("icon") val icon: String,
)

object SearchRetrofit {
    private const val BASE_URL = "https://nominatim.openstreetmap.org/"
    val api: LocationApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(LocationApi::class.java)
    }
}