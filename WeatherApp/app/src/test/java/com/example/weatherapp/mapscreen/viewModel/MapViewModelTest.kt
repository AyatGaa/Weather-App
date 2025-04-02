package com.example.weatherapp.mapscreen.viewModel

import com.example.weatherapp.data.models.WeatherAlert
import com.example.weatherapp.data.repository.WeatherRepository
import com.google.android.gms.maps.model.LatLng
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test


class MapViewModelTest{

     lateinit var mapViewModel:MapViewModel
     lateinit var repo : WeatherRepository
    @Before
    fun setup(){
        repo = mockk()
         mapViewModel = MapViewModel()
    }
    @Before
    fun setupTwo() = Dispatchers.setMain(StandardTestDispatcher())

    @After
    fun closeResources() = Dispatchers.resetMain()



    @Test
    fun onPlaceSelectedAndGetLatLng() {
        // Given
        val expectedLatLng = LatLng(0.0, 0.0)
        var result: LatLng? = null

        // When
        mapViewModel.onPlaceSelected(0.0, 0.0) { latLng ->
            result = latLng
        }

        // Then
        assertEquals(expectedLatLng, result)
    }



}

