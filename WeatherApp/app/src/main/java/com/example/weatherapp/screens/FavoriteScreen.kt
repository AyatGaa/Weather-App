package com.example.weatherapp.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.weatherapp.data.models.CityLocation
import com.example.weatherapp.favorite.FavoriteScreenViewModel


 @Composable
fun  Favourite(viewModel: FavoriteScreenViewModel){
    Text(text= "FAvvvv",modifier = Modifier.padding(30.dp),)

    Button(
        modifier = Modifier.padding(32.dp),
        onClick = {

            val city  = CityLocation(
                latitude =26.159377,
                locationId = 1,
                longitude = 32.7308654,
                locationName = "qqqqq",
                country = "Egyyy",
                flag = "to be added" )
            viewModel.addFavouriteLocation(city)
        }
    ) { Text("Add")}
}