package com.example.weatherapp.screens

import android.util.Log
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.weatherapp.data.models.CityLocation
import com.example.weatherapp.favorite.FavoriteScreenViewModel
import com.example.weatherapp.navigation.ScreenRoute
import com.example.weatherapp.ui.theme.BabyBlue
import com.example.weatherapp.ui.theme.DarkBlue2
import com.example.weatherapp.ui.theme.component.TopAppBar
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.launch


@Composable
fun Favourite(viewModel: FavoriteScreenViewModel, onFabMapClick:()->Unit) {
  //  val navController = rememberNavController()
    val snackBarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val lat = remember { mutableStateOf(" hh") }
    Scaffold(
        snackbarHost = { SnackbarHost(snackBarHostState) },
        modifier = Modifier
            .statusBarsPadding()
            .navigationBarsPadding(),
        topBar = { TopAppBar("Favorites") },

        floatingActionButtonPosition = FabPosition.EndOverlay,
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    Log.i("TAG", "Favourite: before NAV")
                 //   navController.navigate(ScreenRoute.MapScreen){ }
                    onFabMapClick()
                    Log.i("TAG", "Favourite: after NAV")
                    scope.launch {
                        snackBarHostState.showSnackbar("fab clicked")
                    }
                },
                shape = CircleShape,
                containerColor = BabyBlue,
                contentColor = DarkBlue2,
                modifier = Modifier
                    .padding(bottom = 20.dp),


                ) {

                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = "addLocation"
                )
            }

        },

        ) { pad ->



            Text(
                text = lat.toString(),
                modifier = Modifier.padding(pad)
            )


    }
}