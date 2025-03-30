package com.example.weatherapp.setting.uicomponent

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
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
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.example.weatherapp.R
import com.example.weatherapp.ui.theme.DarkBlue2
import com.example.weatherapp.ui.theme.Gray
import com.example.weatherapp.utils.SharedObject


@Composable
fun LanguageSection() {
    val langOptions = listOf("English", "Arabic")
    val context = LocalContext.current

    var selectedLang by remember {
        mutableStateOf(SharedObject.getString("lang","en"))
    }

    Column(
        modifier = Modifier.selectableGroup()
    ) {
        MainHeader("Language", R.drawable.language)

        langOptions.forEach { text ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(start = 24.dp)
                    .selectable(
                        selected = (selectedLang == text),
                        onClick = {
                            selectedLang = text
                            SharedObject.saveString("lang", selectedLang)
                            Toast.makeText(
                                context,
                                "Language set to $selectedLang",
                                Toast.LENGTH_SHORT
                            ).show()
                        },
                        role = Role.RadioButton
                    )
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = (selectedLang == text),
                    onClick = null,
                    colors = RadioButtonDefaults.colors(
                        selectedColor = DarkBlue2,
                        unselectedColor = Gray
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
