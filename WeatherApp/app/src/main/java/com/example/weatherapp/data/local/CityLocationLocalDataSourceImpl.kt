package com.example.weatherapp.data.local

import com.example.weatherapp.data.models.CityLocation
import kotlinx.coroutines.flow.Flow

class CityLocationLocalDataSourceImpl(private val dao:CityLocationDao) : CityLocationLocalDataSource {
    override suspend fun getFavouriteCityLocations(): Flow<List<CityLocation>> {
        return dao.getFavouriteCityLocations()
    }

    override suspend fun insertCityLocation(cityLocation: CityLocation): Long {
        return dao.insertCityLocation(cityLocation)
    }

    override suspend fun deleteCityLocation(cityLocation: CityLocation): Int {
        return dao.deleteCityLocation(cityLocation)
    }
}