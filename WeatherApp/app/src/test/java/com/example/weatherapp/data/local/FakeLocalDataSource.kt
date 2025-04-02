package com.example.weatherapp.data.local

import com.example.weatherapp.data.models.CityLocation
import com.example.weatherapp.data.models.HomeEntity
import com.example.weatherapp.data.models.WeatherAlert
import kotlinx.coroutines.flow.Flow

class FakeLocalDataSource(private var cityList: MutableList<CityLocation> = mutableListOf()) :
    CityLocationLocalDataSource {


    override suspend fun getFavouriteCityLocations(): Flow<List<CityLocation>> {
        TODO("Not yet implemented")
    }

    override suspend fun getCityById(cityId: Int): CityLocation {
        return cityList.find { it.id == cityId } ?: throw NoSuchElementException("City not found")

     }

    override suspend fun insertCityLocation(cityLocation: CityLocation): Long {
        if (cityList.any { it.id == cityLocation.id }) {
            return -1
        }
        cityList.add(cityLocation)
        return cityLocation.id.toLong()
    }


    override suspend fun deleteCityLocation(cityLocation: CityLocation): Int {
        var result = 0
        if (cityList.remove(cityLocation)) {
            result = 1
        }
        return result
    }

    override suspend fun insertAlert(alert: WeatherAlert): Long {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAlert(alert: WeatherAlert): Int {
        TODO("Not yet implemented")
    }

    override suspend fun getAllWeatherAlert(): Flow<List<WeatherAlert>> {
        TODO("Not yet implemented")
    }

    override suspend fun getAlertByID(id: Int): WeatherAlert {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAlertById(alertId: Int): Int {
        TODO("Not yet implemented")
    }

    override suspend fun getAlertByTime(start: Long, end: Long): WeatherAlert? {
        TODO("Not yet implemented")
    }

    override suspend fun getHomeData(): HomeEntity {
        TODO("Not yet implemented")
    }

    override suspend fun insertHomeData(homeEntity: HomeEntity): Long {
        TODO("Not yet implemented")
    }
}