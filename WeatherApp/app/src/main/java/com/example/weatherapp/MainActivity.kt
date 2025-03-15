package com.example.weatherapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import com.example.weatherapp.remote.RetrofitHelper
import com.example.weatherapp.ui.theme.WeatherAppTheme
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
       setContent {
            WeatherAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }

       lifecycleScope.launch  {
           try {
               val response = RetrofitHelper.service.getCurrentWeather(
                   lat = 34.34,
                   lon = 10.99,
                   units = "metric",
                   language = "en",
                   apiKey = BuildConfig.apiKeySafe
               )

               if (response.isSuccessful) {
                   val weatherData = response.body()
                   Log.d("TAG", weatherData.toString())
               } else {
                   Log.e("TAG", "Response Error: ${response.errorBody()?.string()}")
               }
           } catch (e: Exception) {
               Log.e("TAG", "Exception: ${e.message}")
           }
        }


    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    WeatherAppTheme {
        Greeting("Android")
    }
}