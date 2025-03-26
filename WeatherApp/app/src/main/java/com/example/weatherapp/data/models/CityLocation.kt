package com.example.weatherapp.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "city_location" ,primaryKeys = ["latitude", "longitude", "locationId"])
data class CityLocation(

    val locationId: Int,

    val latitude: Double,
    val longitude: Double,
    val locationName: String,//maybe city
    val country: String,
    val flag: String,
)
