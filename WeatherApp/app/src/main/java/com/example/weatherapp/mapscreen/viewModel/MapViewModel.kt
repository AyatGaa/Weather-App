package com.example.weatherapp.mapscreen.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import java.net.URL


class MapViewModel : ViewModel() {

    private val _searchResults = MutableSharedFlow<List<LocationData>>(replay = 1)
    val searchResults = _searchResults

    private val searchQueryFlow = MutableSharedFlow<String>(replay = 0)


    init {
        viewModelScope.launch {
            searchQueryFlow
                .debounce(300) // Wait 300ms before making API call
                .distinctUntilChanged()
                .collectLatest { query ->
                    fetchAutocompleteResults(query)
                }
        }
    }

    fun onSearch(query: String) {
        viewModelScope.launch {
            searchQueryFlow.emit(query) // Emit query to flow
        }
    }

    fun onPlaceSelected(lat: Double, lon: Double, onLocationSelected: (LatLng) -> Unit) {
        val latLng = LatLng(lat, lon)
        onLocationSelected(latLng)
    }

    private fun fetchAutocompleteResults(query: String) {
        viewModelScope.launch {
            if (query.isEmpty()) {
                _searchResults.emit(emptyList())
                return@launch
            }

            try {
                val url = "https://nominatim.openstreetmap.org/search?format=json&q=${query}"
                val response = withContext(Dispatchers.IO) {
                    URL(url).readText()
                }
                val results = JSONArray(response).let { jsonArray ->
                    (0 until jsonArray.length()).map { i ->
                        jsonArray.getJSONObject(i).let {
                            LocationData(
                                name = it.getString("display_name"),
                                lat = it.getDouble("lat"),
                                lon = it.getDouble("lon")
                            )
                        }
                    }
                }
                _searchResults.emit(results)
            } catch (e: Exception) {
                Log.e("Search", "Error fetching predictions: ${e.message}")
                _searchResults.emit(emptyList())
            }
        }
    }

    data class LocationData(val name: String, val lat: Double, val lon: Double)

}
