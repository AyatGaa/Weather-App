package com.example.weatherapp.data.local

import com.example.weatherapp.data.models.CityLocation
import com.example.weatherapp.data.models.WeatherAlert
import kotlinx.coroutines.flow.Flow

class CityLocationLocalDataSourceImpl(private val dao: CityLocationDao) :
    CityLocationLocalDataSource {
    override suspend fun getFavouriteCityLocations(): Flow<List<CityLocation>> {
        return dao.getFavouriteCityLocations()
    }

    override suspend fun getCityById(cityId: Int): CityLocation {
        return dao.getCityById(cityId)
    }

    override suspend fun insertCityLocation(cityLocation: CityLocation): Long {
        return dao.insertCityLocation(cityLocation)
    }

    override suspend fun deleteCityLocation(cityLocation: CityLocation): Int {
        return dao.deleteCityLocation(cityLocation)
    }

    override suspend fun insertAlert(alert: WeatherAlert): Long {
        return dao.insertAlert(alert)
    }

    override suspend fun deleteAlert(alert: WeatherAlert): Int {
        return dao.deleteAlert(alert)
    }

    override suspend fun getAllWeatherAlert(): Flow<List<WeatherAlert>> {
        return dao.getAllWeatherAlert()
    }

    override suspend fun getAlertByID( id: Int):WeatherAlert {
        return dao.getAlertByID( id)
    }

    override suspend fun deleteAlertById(alertId: Int): Int {
        return dao.deleteAlertById(alertId)
    }

    override suspend fun getAlertByTime(start: Long, end: Long): WeatherAlert? {
        return dao.getAlertByTime(start,end)
    }

}