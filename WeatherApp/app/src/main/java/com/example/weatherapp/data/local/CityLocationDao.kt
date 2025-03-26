package com.example.weatherapp.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.weatherapp.data.models.CityLocation
import kotlinx.coroutines.flow.Flow


@Dao
interface CityLocationDao {

    @Query("SELECT * FROM city_location")
    fun getFavouriteCityLocations(): Flow<List<CityLocation>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCityLocation(cityLocation: CityLocation): Long

    @Delete
    suspend fun deleteCityLocation(cityLocation: CityLocation): Int
}
