package com.example.weatherapp.repository

import androidx.room.Room

import com.example.weatherapp.data.local.CityDatabase
import com.example.weatherapp.data.local.CityLocationDao
import com.example.weatherapp.data.local.CityLocationLocalDataSource
import com.example.weatherapp.data.local.CityLocationLocalDataSourceImpl
import com.example.weatherapp.data.local.FakeLocalDataSource
import com.example.weatherapp.data.local.FakeRemoteDataSource
import com.example.weatherapp.data.models.CityLocation
import com.example.weatherapp.data.models.CityResponse
import com.example.weatherapp.data.models.CurrentResponseApi
import com.example.weatherapp.data.models.ForecastItem
import com.example.weatherapp.data.models.ForecastResponseApi
import com.example.weatherapp.data.models.Main
import com.example.weatherapp.data.models.Weather
import com.example.weatherapp.data.models.Wind
import com.example.weatherapp.data.remote.WeatherRemoteDataSource
import com.example.weatherapp.data.repository.WeatherRepository
import com.example.weatherapp.data.repository.WeatherRepositoryImpl
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Assert
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


class WeatherRepositoryTest {

    private lateinit var localDataSource: CityLocationLocalDataSource
    private lateinit var remoteDataSource: WeatherRemoteDataSource
    private lateinit var cityDatabase: CityDatabase
    private lateinit var dao: CityLocationDao
    private lateinit var repository: WeatherRepository

    private val cityLocation1 = CityLocation(
        1,
        CityResponse.CityResponseItem("cityName", 0.0, CityResponse.CityResponseItem.LocalNames(null, null, null), 0.0, "name"),
        0.0,
        currentWeather = mockCurrentWeather(),
        lon = 0.0,
        forecastWeather = mockForecastWeather(),
        flag = "flag",
    )
    private val cityLocation2 = CityLocation(
        2,
        CityResponse.CityResponseItem("cityName", 0.0, CityResponse.CityResponseItem.LocalNames(null, null, null), 0.0, "name"),
        0.0,
        currentWeather = mockCurrentWeather(),
        lon = 0.0,
        forecastWeather = mockForecastWeather(),
        flag = "flag",
    )

    private val cityLocation3 = CityLocation(
        3,
        CityResponse.CityResponseItem("cityName", 0.0, CityResponse.CityResponseItem.LocalNames(null, null, null), 0.0, "name"),
        0.0,
        currentWeather = mockCurrentWeather(),
        lon = 0.0,
        forecastWeather = mockForecastWeather(),
        flag = "flag",
    )

    private val cityLocation4 = CityLocation(
        4,
        CityResponse.CityResponseItem("cityName", 0.0, CityResponse.CityResponseItem.LocalNames(null, null, null), 0.0, "name"),
        0.0,
        currentWeather = mockCurrentWeather(),
        lon = 0.0,
        forecastWeather = mockForecastWeather(),
        flag = "flag",
    )

    private val currentWeather = mockCurrentWeather()

    private var localCities = listOf(cityLocation1, cityLocation2, cityLocation4, cityLocation3)
    private var remoteData = currentWeather

    //set up our env
    @Before
    fun setup() {

        localDataSource = FakeLocalDataSource(localCities.toMutableList())
        remoteDataSource = FakeRemoteDataSource(remoteData)
        repository = WeatherRepositoryImpl(
            localDataSource = localDataSource,
            remoteDataSource = remoteDataSource
        )
    }


    @Test
    fun getCityLocationById() = runTest {
        // When, call getCty by Id using repository
        val city = repository.getCityById(2)


        //Then the object retrievd form local is the same is exist
        assertThat(city, `is`(localCities.get(1)))
    }


    @Test
    fun insertCityLocationAndGetCityLocationById() = runTest {
        //Given, create city object to insert
        val city = CityLocation(5, CityResponse.CityResponseItem("cityName", 0.0, CityResponse.CityResponseItem.LocalNames(null, null, null), 0.0, "name"), 0.0, currentWeather = mockCurrentWeather(), lon = 0.0, forecastWeather = mockForecastWeather(), flag = "flag",)
        //When Insert it using repo
        val result = repository.insertCityLocation(city)


        // Then, data returned correctly
        assertThat(result, `is`(city.id.toLong()))

    }

    @Test
    fun getCurrentWeatherData() = runTest {
        //Given, prepare mock Data
        val mockData = mockCurrentWeather()

        //When call data 'fake API response'
        val result = repository.getCurrentWeather(0.0, 0.0, "en", "Standard").first()

        //Then retreived the data
        assertNotNull(result)
        assertEquals(mockData.base, result.base)
        assertEquals(mockData.cod, result.cod)
    }

}


fun mockCurrentWeather(): CurrentResponseApi {
    return CurrentResponseApi(
        base = "base",
        clouds = CurrentResponseApi.Clouds(5),
        cod = 0,
        coord = CurrentResponseApi.Coord(0.0, 0.0),
        dt = 5,
        id = 5,
        main = null,
        name = null,
        rain = null,
        sys = null,
        timezone = null,
        visibility = null,
        weather = null,
        wind = null
    )
}

fun mockForecastWeather(): ForecastResponseApi {
    return ForecastResponseApi(
        list = listOf(
            ForecastItem(
                timestamp = 10000,
                main = Main(
                    null, null, null, null, null, null, null, null
                ),
                weather = listOf(
                    Weather("des", null, null, null)
                ),
                wind = Wind(null, null, null)
            ),
            ForecastItem(
                timestamp = 10000,
                main = Main(
                    null, null, null, null, null, null, null, null
                ),
                weather = listOf(
                    Weather("des", null, null, null)
                ),
                wind = Wind(null, null, null)
            )
        ),
    )
}