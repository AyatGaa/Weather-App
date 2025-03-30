package com.example.weatherapp

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.weatherapp.navigation.BottomNavigationBar
import com.example.weatherapp.navigation.SetupNavHost
import com.example.weatherapp.utils.SharedObject

class MainActivity : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {

        val viewModel by viewModels<SplashViewModel>()
        lateinit var navController: NavHostController

        super.onCreate(savedInstanceState)
        setContent {

            enableEdgeToEdge()
            actionBar?.hide()
            installSplashScreen()
                .apply {
                    actionBar?.hide()

                    navController = rememberNavController()
                    SharedObject.init(this@MainActivity)

                    setKeepOnScreenCondition {
                        viewModel.isLoading.value
                    }

                }

            Scaffold(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 32.dp),
                bottomBar = {
                    BottomNavigationBar(
                        navController
                    )
                }
            ) { pad ->
                actionBar?.hide()
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


