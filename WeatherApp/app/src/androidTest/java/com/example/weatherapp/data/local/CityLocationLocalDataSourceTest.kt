package com.example.weatherapp.data.local

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.example.weatherapp.data.models.CityLocation
import com.example.weatherapp.data.models.CityResponse
import com.example.weatherapp.data.models.CurrentResponseApi
import com.example.weatherapp.data.models.ForecastItem
import com.example.weatherapp.data.models.ForecastResponseApi
import com.example.weatherapp.data.models.Main
import com.example.weatherapp.data.models.Weather
import com.example.weatherapp.data.models.WeatherAlert
import com.example.weatherapp.data.models.Wind
import junit.framework.TestCase.assertNull
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@MediumTest
@RunWith(AndroidJUnit4::class)
class CityLocationLocalDataSourceTest {
    private lateinit var localDataSource: CityLocationLocalDataSource
    private lateinit var cityDatabase: CityDatabase
    private lateinit var  dao:CityLocationDao

    //set up our env
    @Before
    fun setup(){
        cityDatabase = Room.inMemoryDatabaseBuilder( // local DB for testing
            ApplicationProvider.getApplicationContext(),
            CityDatabase::class.java
        ).build()
        dao = cityDatabase.getCityDao()
        localDataSource = CityLocationLocalDataSourceImpl(dao)

    }

    @After
    fun closeDatabase(){
        cityDatabase.close()
    }


    // Alert Test
    @Test
    fun insertWeatherAlertAndGetWeatherAlert() = runTest {
        //Given , create cityLocation object
        val  weatherAlert = WeatherAlert(
            5,
            "cityName",
            100000L,
            100000L,
            0.0,
            0.0
        )

        localDataSource.insertAlert(weatherAlert)

        //When, get the alert
        val result = localDataSource.getAlertByID(weatherAlert.id)

        // Then, it inserted and retrieved
        assertNotNull(result )
        assertThat(result.id, `is`(weatherAlert.id))
        assertThat(result.cityName, `is`(weatherAlert.cityName))
        assertThat(result.endDate, `is`(weatherAlert.endDate))
        assertThat(result.startDate, `is`(weatherAlert.startDate))
    }


    //Favorite Test
    @Test
    fun insertCityLocationGetByIdAndGetCityById() = runTest {
        // Given , create cityLocation object wih mocks
        val city = CityLocation(
            id = 5,
            cityData = mockCityData(),
            lat = 30.0,
            lon = 31.0,
            currentWeather = mockCurrentWeather(),
            forecastWeather = mockForecastWeather(),
            flag = "EG"
        )

        localDataSource.insertCityLocation(city)

        //When, get the object from db
        val result = localDataSource.getCityById(5)


        // Then, it inserted and retrieved
        assertNotNull(city )
        assertThat(result.id, `is`(city.id))
        assertThat(result.lat, `is`(city.lat))
        assertThat(result.lon, `is`(city.lon))
        assertThat(result.cityData.name, `is`(city.cityData.name))
    }

    //delete Alert Test

    @Test
    fun deleteAlertTest() = runTest{
        //Given , create cityLocation object and insert it
        val  weatherAlert = WeatherAlert(
            5,
            "cityName",
            100000L,
            100000L,
            0.0,
            0.0
        )
        localDataSource.insertAlert(weatherAlert)

        //When, delete the alert and get number of deleted rows
        val result = localDataSource.deleteAlertById(weatherAlert.id)

        // Then, deleted assertion by 1
        assertThat(result, `is`(1))

        //return null of removed row
        val resultAfterDelete = localDataSource.getAlertByID(weatherAlert.id)
        assertNull(resultAfterDelete)

    }


    // Mock Data Functions
    private fun mockCityData(): CityResponse.CityResponseItem {
        return CityResponse.CityResponseItem(
            name = "Cairo",
            country = "EG",
            lat = 0.0,
            lon = 0.0,

            localNames =  CityResponse.CityResponseItem.LocalNames("ar",null,null)
        )
    }

    private fun mockCurrentWeather(): CurrentResponseApi {
        return CurrentResponseApi(
            base = "base",
            clouds = CurrentResponseApi.Clouds(5),
            cod = 0,
            coord = CurrentResponseApi.Coord(0.0,0.0),
            dt = 5,
            id = 5,
            main = null,
            name = null,
            rain = null,
            sys = null,
            timezone =  null,
            visibility = null,
            weather = null,
            wind = null
        )
    }

    private fun mockForecastWeather(): ForecastResponseApi {
        return ForecastResponseApi(
            list = listOf(
                ForecastItem(
                    timestamp = 10000,
                    main = Main(null
                        ,null,null,null,null,null,null,null
                    ),
                    weather = listOf(
                        Weather("des", null,null,null)
                    ),
                    wind = Wind(null,null,null)
                ),
                ForecastItem(  timestamp = 10000,
                    main = Main(null
                        ,null,null,null,null,null,null,null
                    ),
                    weather = listOf(
                        Weather("des", null,null,null)
                    ),
                    wind = Wind(null,null,null)
                )
            ),
        )
    }
}
