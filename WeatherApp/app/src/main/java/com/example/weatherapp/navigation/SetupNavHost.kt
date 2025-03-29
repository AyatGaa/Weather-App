package com.example.weatherapp.navigation

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.weatherapp.data.local.CityDatabase
import com.example.weatherapp.data.local.CityLocationLocalDataSourceImpl
import com.example.weatherapp.data.remote.RetrofitHelper
import com.example.weatherapp.data.remote.WeatherRemoteDataSourceImpl
import com.example.weatherapp.data.repository.WeatherRepositoryImpl
import com.example.weatherapp.favorite.viewModel.FavoriteLocationFactory
import com.example.weatherapp.favorite.viewModel.FavoriteScreenViewModel
import com.example.weatherapp.mapscreen.viewModel.MapViewModel
import com.example.weatherapp.screens.Alert
import com.example.weatherapp.favorite.view.Favourite
import com.example.weatherapp.homescreen.view.HomeScreen
import com.example.weatherapp.homescreen.viewmodel.CurrentWeatherFactory
import com.example.weatherapp.setting.Setting
import com.example.weatherapp.homescreen.viewmodel.HomeScreenViewModel
import com.example.weatherapp.mapscreen.view.MapScreen
import com.google.android.gms.maps.model.LatLng
const val LOCATION_RESULT_KEY = "location_result"

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SetupNavHost(navController: NavHostController
 ) {

    NavHost(
        navController = navController,
        startDestination = ScreenRoute.Home
    ) {
        composable<ScreenRoute.Home> {
        val context = LocalContext.current

            val homeFactory = CurrentWeatherFactory(
                repo = WeatherRepositoryImpl.getInstance(
                    WeatherRemoteDataSourceImpl(
                        RetrofitHelper.service
                    ),
                    CityLocationLocalDataSourceImpl(
                        CityDatabase.getInstance(context).getCityDao()
                    )
                )
            )
            val viewModelHome:HomeScreenViewModel =  viewModel(factory = homeFactory)

            HomeScreen (viewModelHome){}
        }

        composable<ScreenRoute.Favorites> {
            val context = LocalContext.current

            val favoriteFactory = FavoriteLocationFactory(
                repo = WeatherRepositoryImpl.getInstance(
                    WeatherRemoteDataSourceImpl(
                        RetrofitHelper.service
                    ),
                    CityLocationLocalDataSourceImpl(
                        CityDatabase.getInstance(context).getCityDao()
                    )
                )
            )
            val viewModelFavorite : FavoriteScreenViewModel =  viewModel(factory = favoriteFactory)

            val profile = it.toRoute<ScreenRoute.Favorites>( )
            Log.d("TAG", "SetupNavHost FAVVV: LatLong PROFILE ${profile.lat},,${profile.lon}")

            val latLng = LatLng(profile.lat,profile.lon)

            Favourite(viewModelFavorite, latLng){
                navController.navigate(ScreenRoute.MapScreen)
            }
        }


        composable<ScreenRoute.Alerts> {
            val profile = it.toRoute<ScreenRoute.Alerts>( )
            Alert(){}
        }
        composable<ScreenRoute.Settings> {
            val profile = it.toRoute<ScreenRoute.Settings>( )
            Setting ()
        }


        composable<ScreenRoute.MapScreen> {
                val profile = it.toRoute<ScreenRoute.Favorites>( )
            val viewModelMap: MapViewModel =  viewModel()

            MapScreen(viewModelMap) {locationFromMap->
//                navController.previousBackStackEntry
//                    ?.savedStateHandle
//                    ?.set(LOCATION_RESULT_KEY, LatLng(it.latitude, it.longitude))

                navController.popBackStack()
                navController.navigate(ScreenRoute.Favorites(locationFromMap.latitude.toDouble(), locationFromMap.longitude))
            }

                Log.w("TAG", "SetupNavHost: inMAP Screen", )

        }


     }
}
/*
composable<ScreenRoutes.FavLocScreen> {
    val favoritesViewModel: FavViewModel = viewModel(
        factory = remember {
            FavViewModelFactory(
                RepoImpl(
                    RemoteDataSourceImpl(RetrofitHelper.service),
                    LocalDataSourceImpl(
                        WeatherDataBase.getInstance(context).getWeatherDao()
                    ),
                )
            )
        }
    )
    FavLocUI(
        navigateToFavDetails = { lon, lat ->
            navController.navigate(ScreenRoutes.FavDetailsScreen(lon, lat))
        },
        favoritesViewModel,
        navigateToMap = { navController.navigate(ScreenRoutes.MapScreenFromFavorites) }
    )
}*/
