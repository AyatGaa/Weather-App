package com.example.weatherapp.data.models


import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
class CityResponse : ArrayList<CityResponse.CityResponseItem>(){
    @Serializable
    data class CityResponseItem(
        @SerializedName("country")
        val country: String?,
        @SerializedName("lat")
        val lat: Double?,
        @SerializedName("local_names")
        val localNames: LocalNames?,
        @SerializedName("lon")
        val lon: Double?,
        @SerializedName("name")
        val name: String?
    ) {
        @Serializable
        data class LocalNames(
            @SerializedName("ar")
            val ar: String?,
            @SerializedName("ascii")
            val ascii: String?,
            @SerializedName("en")
            val en: String?,
        )
    }
}