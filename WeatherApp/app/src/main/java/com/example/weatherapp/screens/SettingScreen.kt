package com.example.weatherapp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonColors
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weatherapp.ui.theme.BabyBlue
import com.example.weatherapp.ui.theme.DarkBlue1
import com.example.weatherapp.ui.theme.DarkBlue2
import com.example.weatherapp.ui.theme.Gray
import com.example.weatherapp.ui.theme.White
import com.example.weatherapp.ui.theme.Yellow

@Composable
fun TopAppBar(title: String) {
    Box(
        modifier = Modifier
            .background(Color.Transparent)
            .padding(8.dp)
            .fillMaxWidth(), contentAlignment = Alignment.Center
    ) {

        Text(
            text = title,
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            color = DarkBlue2,
            letterSpacing = 1.sp,
        )

    }
}

@Preview
@Composable
fun Setting() {
    Scaffold(

        containerColor = BabyBlue,
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 32.dp)
            .background(BabyBlue),
        topBar = { TopAppBar("Setting") }
    ) { pad ->

        Spacer(modifier = Modifier.height(16.dp))
        Column(modifier = Modifier.padding(pad)) {
            LocationSection()
            LanguageSection()
            UnitsSection()
        }
        Spacer(modifier = Modifier.height(16.dp))
    }

}

@Preview
@Composable
fun LocationSection() {
    val locationOptions = listOf("GPS", "Map")
    val (selectedLoc, onLocationSelected) = remember { mutableStateOf(locationOptions[0]) }

    Column(
        modifier = Modifier
            .selectableGroup()
    ) {

        MainHeader("Location", Icons.Default.LocationOn)

        locationOptions.forEach { text ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .padding(start = 24.dp)
                    .selectable(
                        selected = (text == selectedLoc),
                        onClick = { onLocationSelected(text) },
                        role = Role.RadioButton
                    )
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically

            ) {

                RadioButton(
                    selected = (text == selectedLoc),
                    onClick = null,
                    colors = RadioButtonColors(
                        selectedColor = DarkBlue2,
                        unselectedColor = Gray,
                        disabledSelectedColor = DarkBlue2,
                        disabledUnselectedColor = Gray
                    ),
                )
                Text(
                    text = text,
                    modifier = Modifier.padding(start = 4.dp)
                )
            }

        }

    }
}


@Preview
@Composable
fun LanguageSection() {
    val langOptions = listOf("Arabic", "English")
    val (selectedLang, onLangSelected) = remember { mutableStateOf(langOptions[0]) }

    Column(
        modifier = Modifier
            .selectableGroup()
    ) {

        MainHeader("Language", Icons.Default.Star)

        langOptions.forEach { text ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .padding(start = 24.dp)
                    .selectable(
                        selected = (text == selectedLang),
                        onClick = { onLangSelected(text) },
                        role = Role.RadioButton
                    )
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically

            ) {
                RadioButton(
                    selected = (text == selectedLang),
                    onClick = null,
                    colors = RadioButtonColors(
                        selectedColor = DarkBlue2,
                        unselectedColor = Gray,
                        disabledSelectedColor = DarkBlue2,
                        disabledUnselectedColor = Gray
                    ),
                )
                Text(
                    text = text,
                    modifier = Modifier.padding(start = 4.dp)
                )
            }

        }

    }
}

@Preview
@Composable
fun UnitsSection() {
    val tempOptions = listOf("Kelvin", "Celsius", "Fahrenheit")
    val speedOptions = listOf("Meter/Sec (m/sec)", "Miles/Hour (mph)")

    val (selectedTemp, onTempSelected) = remember { mutableStateOf(tempOptions[0]) }
    val (selectedSpeed, onSpeedSelected) = remember { mutableStateOf(speedOptions[0]) }

    Column(
        modifier = Modifier
            .selectableGroup()
    ) {

        MainHeader("Units", Icons.Default.Build)

        Spacer(modifier = Modifier.height(10.dp))
        SectionHeader("Temperature Unit")
        tempOptions.forEach { text ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .padding(start = 24.dp)
                    .selectable(
                        selected = (text == selectedTemp),
                        onClick = { onTempSelected(text) },
                        role = Role.RadioButton
                    )
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically

            ) {

                RadioButton(
                    selected = (text == selectedTemp),
                    onClick = null,
                    colors = RadioButtonColors(
                        selectedColor = DarkBlue2,
                        unselectedColor = Gray,
                        disabledSelectedColor = DarkBlue2,
                        disabledUnselectedColor = Gray
                    ),
                )
                Text(
                    text = text,
                    modifier = Modifier.padding(start = 4.dp)
                )
            }


        }
        SectionHeader("Wind Speed")
        speedOptions.forEach { text ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .padding(start = 24.dp)
                    .selectable(
                        selected = (text == selectedSpeed),
                        onClick = { onSpeedSelected(text) },
                        role = Role.RadioButton
                    )
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically

            ) {

                RadioButton(
                    selected = (text == selectedSpeed),
                    onClick = null,
                    colors = RadioButtonColors(
                        selectedColor = DarkBlue2,
                        unselectedColor = Gray,
                        disabledSelectedColor = DarkBlue2,
                        disabledUnselectedColor = Gray
                    ),
                )
                Text(
                    text = text,
                    modifier = Modifier.padding(start = 4.dp)
                )
            }
        }
    }

}


@Composable
fun SectionHeader(title: String) {
    Row(
        modifier = Modifier
            .padding(start = 16.dp, end = 16.dp)

            .clip(RoundedCornerShape(bottomEnd = 8.dp, bottomStart = 8.dp))
            .background(Yellow)
            .shadow(40.dp)
            .padding(8.dp)
            .fillMaxWidth()
            .wrapContentSize(Alignment.CenterStart)
    ) {
        Text(
            text = title,
            fontSize = 16.sp,
            color = DarkBlue2
        )

    }

}

@Composable
fun MainHeader(title: String, icon: ImageVector?) {
    Row(
        modifier = Modifier

            .background(DarkBlue1)
            .padding(8.dp)
            .fillMaxWidth()
            .wrapContentSize(Alignment.CenterStart)
    ) {
        icon?.let {
            Icon(
                modifier = Modifier.size(18.dp),
                imageVector = it,
                tint = White,
                contentDescription = "location logo"
            )
        }
        Text(
            text = title,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = White
        )

    }

}
