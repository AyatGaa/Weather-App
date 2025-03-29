package com.example.weatherapp.mapscreen.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.utils.search.LocationData
import com.example.weatherapp.utils.search.SearchRetrofit
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.URLEncoder


class MapViewModel : ViewModel() {

    private val _searchResults = MutableSharedFlow<List<LocationData>>(replay = 1)
    val searchResults = _searchResults
    private val searchQueryFlow = MutableSharedFlow<String>(replay = 0)

    init {
        viewModelScope.launch {
            searchQueryFlow
                .debounce(300) // estnaaa
                .distinctUntilChanged()
                .collectLatest { query ->
                    fetchAutocompleteResults(query)
                }
        }
    }

    fun onSearch(query: String) {
        viewModelScope.launch {
            searchQueryFlow.emit(query)
        }
    }

    fun onPlaceSelected(lat: Double, lon: Double, onLocationSelected: (LatLng) -> Unit) {
        val latLng = LatLng(lat, lon)
        onLocationSelected(latLng)
    }

    private fun fetchAutocompleteResults(query: String) {
        viewModelScope.launch {
            if (query.isBlank()) {
                _searchResults.emit(emptyList())
                return@launch
            }

            try {
                val encodedQuery = URLEncoder.encode(query, "UTF-8")
                val results = withContext(Dispatchers.IO) {
                    SearchRetrofit.api.getLocations(encodedQuery)
                }.distinctBy { "${it.lat},${it.lon}" }

                _searchResults.emit(results)
            } catch (e: Exception) {
                Log.e("Search", "Error fetching predictions: ${e.message}")
                _searchResults.emit(emptyList())
            }
        }
    }

}
