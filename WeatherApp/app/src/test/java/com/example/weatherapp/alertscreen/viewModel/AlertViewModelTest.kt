package com.example.weatherapp.alertscreen.viewModel

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.weatherapp.data.models.WeatherAlert
import com.example.weatherapp.data.repository.WeatherRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.hamcrest.CoreMatchers.not
import org.hamcrest.CoreMatchers.nullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
class AlertViewModelTest {
    lateinit var repo: WeatherRepository
    lateinit var alertViewModel: AlertViewModel


    @Before
    fun setup() {
        repo = mockk(relaxed = true)
        alertViewModel = AlertViewModel(repo)

    }

    @Before
    fun setupTwo() = Dispatchers.setMain(StandardTestDispatcher())

    @After
    fun closeResources() = Dispatchers.resetMain()

    @Test
    fun getAlerts() {
        // When get all alert
        alertViewModel.getAllAlerts()

        //the observer on data inside the function not null
        val result = alertViewModel.alertFlow.value
        assertThat(result, not(nullValue()))

    }


    @Test
    fun deleteAlertUsingViewModel() = runTest {
        // Given
        val mockAlert = WeatherAlert(
            id = 1,
            lat = 10.0,
            lon = 20.0,
            startDate = 1000L,
            endDate = 2000L,
            cityName = "Test City"
        )
        coEvery { (repo.deleteAlert(mockAlert)) } returns 1  //as it suuccessful

        // When
        alertViewModel.deleteAlert(mockAlert)

        // Then
        assertEquals("Alert Deleted!", alertViewModel.mutableDatabaseMessage.first())
        coVerify { (repo).deleteAlert(mockAlert) }
    }

}