package com.example.weatherapp.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
 import com.example.weatherapp.data.models.CityLocation
import com.example.weatherapp.data.models.Weather
import com.example.weatherapp.data.models.WeatherAlert
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

    //alert fun
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlert(alert: WeatherAlert) : Long
    @Delete
    suspend fun deleteAlert(alert: WeatherAlert):Int

    @Query("SELECT * FROM alert_table")
      fun getAllWeatherAlert():Flow<List<WeatherAlert>>


    @Query("SELECT * FROM alert_table WHERE  id = :id")
    suspend fun getAlertByID( id:Int):WeatherAlert

}
