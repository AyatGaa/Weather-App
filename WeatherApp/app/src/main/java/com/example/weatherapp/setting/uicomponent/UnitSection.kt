package com.example.weatherapp.setting.uicomponent

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.example.weatherapp.R
import com.example.weatherapp.ui.theme.DarkBlue2
import com.example.weatherapp.ui.theme.Gray
import com.example.weatherapp.utils.SharedObject
import com.example.weatherapp.utils.getSettingType

@Composable
fun UnitsSection() {
    /*
    * Standard => Kelvin -> meter/sec
    * Metric => Celsius => meter/sec
    * Imperial => Fahrenheit => mile/hour
    * */

    val context = LocalContext.current
    val tempOptions = listOf(stringResource(R.string.kelvin),
        stringResource(R.string.celsius), stringResource(R.string.fahrenheit)
    )
    val speedOptions = listOf(
        stringResource(R.string.meter_sec_m_sec),
        stringResource(R.string.miles_hour_mph)
    )


    var selectedTemp by remember {
        mutableStateOf(
            getSettingType(
                SharedObject.getString("lang", "en"),
                "temp",
                SharedObject.getString("temp", context.getString(R.string.kelvin))
            )
        )
    }

    var selectedSpeed by remember {
        mutableStateOf(
            getSettingType(
                SharedObject.getString("lang", "en"),
                "speed",
                SharedObject.getString("speed", context.getString(R.string.meter_sec_m_sec))
            )
        )
    }

    fun updateUnits(temp: String?, speed: String?) {
        if (temp != null) {
            selectedTemp = temp
            selectedSpeed = if (temp == context.getString(R.string.fahrenheit)) context.getString(R.string.miles_hour_mph)  else context.getString(R.string.meter_sec_m_sec)
        } else if (speed != null) {
            selectedSpeed = speed
            selectedTemp = if (speed == context.getString(R.string.miles_hour_mph)) context.getString(R.string.fahrenheit) else context.getString(R.string.kelvin)
        }

        SharedObject.saveString("temp", selectedTemp)
        SharedObject.saveString("speed", selectedSpeed)

    }


    Column(modifier = Modifier.selectableGroup()) {
        MainHeader(stringResource(R.string.units), R.drawable.barometer)

        Spacer(modifier = Modifier.height(10.dp))
        SectionHeader(stringResource(R.string.temperature_unit))

        tempOptions.forEach { temp ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .padding(start = 24.dp)
                    .selectable(
                        selected = (selectedTemp == temp),
                        onClick = { updateUnits(temp, null) },
                        role = Role.RadioButton
                    )
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = (selectedTemp == temp),
                    onClick = { updateUnits(temp, null) },
                    colors = RadioButtonDefaults.colors(
                        selectedColor = DarkBlue2,
                        unselectedColor = Gray
                    ),
                )
                Text(text = temp, modifier = Modifier.padding(start = 4.dp))
            }
        }

        SectionHeader(stringResource(R.string.wind_speed))
        speedOptions.forEach { speed ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .padding(start = 24.dp)
                    .selectable(
                        selected = (selectedSpeed == speed),
                        onClick = { updateUnits(null, speed) },
                        role = Role.RadioButton
                    )
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = (selectedSpeed == speed),
                    onClick = { updateUnits(null, speed) },
                    colors = RadioButtonDefaults.colors(
                        selectedColor = DarkBlue2,
                        unselectedColor = Gray
                    ),
                )
                Text(text = speed, modifier = Modifier.padding(start = 4.dp))
            }
        }
    }
}


