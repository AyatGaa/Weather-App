package com.example.weatherapp.setting.uicomponent

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.example.weatherapp.ui.theme.DarkBlue2
import com.example.weatherapp.ui.theme.Gray


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