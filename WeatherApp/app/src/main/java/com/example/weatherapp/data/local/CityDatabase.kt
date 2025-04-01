package com.example.weatherapp.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.weatherapp.data.models.CityLocation
import com.example.weatherapp.data.models.WeatherAlert


@Database(entities = [CityLocation::class, WeatherAlert::class], version = 2,exportSchema = false)
abstract class CityDatabase :RoomDatabase(){

    abstract fun getCityDao(): CityLocationDao
   // abstract fun  getWeatherAlertDao(): CityDatabase

    companion object {
        private var instance: CityDatabase? = null

        fun getInstance(context: Context): CityDatabase {
            return instance ?: synchronized(this) {
                val INSTANCE = Room.databaseBuilder(
                    context,
                    CityDatabase::class.java,
                    "city_db"
                ).fallbackToDestructiveMigration()
                    .build()
                instance = INSTANCE
                INSTANCE
            }
        }

    }
}