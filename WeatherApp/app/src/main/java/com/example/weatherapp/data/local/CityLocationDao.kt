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

    @Query("SELECT DISTINCT  * FROM city_location")
    fun getFavouriteCityLocations(): Flow<List<CityLocation>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCityLocation(cityLocation: CityLocation): Long

    @Delete
    suspend fun deleteCityLocation(cityLocation: CityLocation): Int

    @Query("SELECT DISTINCT  * FROM city_location WHERE id== :cityId")
    suspend fun getCityById(  cityId:Int) : CityLocation


}
