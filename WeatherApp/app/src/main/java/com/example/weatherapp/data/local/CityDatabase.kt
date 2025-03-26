package com.example.weatherapp.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.weatherapp.data.models.CityLocation


@Database(entities = [CityLocation::class], version = 1)
abstract class CityDatabase :RoomDatabase(){

    abstract fun getCityDao(): CityLocationDao


    companion object {
        private var instance: CityDatabase? = null

        fun getInstance(context: Context): CityDatabase {
            return instance ?: synchronized(this) {
                val INSTANCE = Room.databaseBuilder(
                    context,
                    CityDatabase::class.java,
                    "city_db"
                ).build()
                instance = INSTANCE
                INSTANCE
            }
        }

    }
}