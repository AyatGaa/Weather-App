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
import java.util.Locale

class MainActivity : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {

        val viewModel by viewModels<SplashViewModel>()
        lateinit var navController: NavHostController
        SharedObject.init(this@MainActivity)

        super.onCreate(savedInstanceState)

        applyLanguage(SharedObject.getString("lang", "en"))
        setContent {

            enableEdgeToEdge()
            actionBar?.hide()
            installSplashScreen()
                .apply {
                    actionBar?.hide()

                    navController = rememberNavController()

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

    private fun applyLanguage(languageCode: String) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        val config = resources.configuration
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)
    }
}


