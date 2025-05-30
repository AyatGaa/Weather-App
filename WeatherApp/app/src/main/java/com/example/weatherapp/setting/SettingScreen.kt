package com.example.weatherapp.setting

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.weatherapp.R
import com.example.weatherapp.homescreen.viewmodel.HomeScreenViewModel
import com.example.weatherapp.setting.uicomponent.LanguageSection
import com.example.weatherapp.setting.uicomponent.LocationSection
import com.example.weatherapp.setting.uicomponent.UnitsSection
import com.example.weatherapp.ui.theme.BabyBlue
import com.example.weatherapp.ui.theme.component.TopAppBar

@Composable
fun Setting( ) {
    Scaffold(
        containerColor = BabyBlue,
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 32.dp)
            .background(BabyBlue),
        topBar = { TopAppBar(stringResource(R.string.setting)) }
    ) { pad ->
        LazyColumn(modifier = Modifier.padding(pad)) {
            item { Spacer(modifier = Modifier.height(16.dp)) }
            item { LocationSection() }
            item { LanguageSection() }
            item { UnitsSection() }
            item { Spacer(modifier = Modifier.height(32.dp)) }
        }
    }
  }
