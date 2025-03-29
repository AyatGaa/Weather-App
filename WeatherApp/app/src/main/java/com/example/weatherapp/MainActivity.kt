package com.example.weatherapp

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.example.weatherapp.navigation.BottomNavigationBar
import com.example.weatherapp.navigation.SetupNavHost

import com.example.weatherapp.utils.SharedObject

class MainActivity : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            val navController = rememberNavController()
            SetupNavHost(navController)
            Log.w("TAG", "onCreate: share init")
            SharedObject.init(this)


            Scaffold(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 32.dp),
                bottomBar = {
                    BottomNavigationBar(navController
                    )
                }
            ) { pad ->
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    SetupNavHost(navController)
                }
            }
        }
    }
}


