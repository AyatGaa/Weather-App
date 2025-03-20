package com.example.weatherapp.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = BabyBlue,
    secondary = Yellow,
    tertiary = White,

            background = BabyBlue,
    surface = BabyBlue,
    onPrimary = White,
    onSecondary = White,
    onTertiary = White,
    onBackground = BabyBlue,
    onSurface = BabyBlue,
)

private val LightColorScheme = lightColorScheme(
    primary = DarkBlue1,
    secondary = DarkBlue2,
    tertiary = Black,


// Other default colors to override
    background = DarkBlue1,
    surface = DarkBlue1,
    onPrimary = White,
    onSecondary = White,
    onTertiary = White,
    onBackground = DarkBlue2,
    onSurface = DarkBlue2,

)

@Composable
fun WeatherAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}