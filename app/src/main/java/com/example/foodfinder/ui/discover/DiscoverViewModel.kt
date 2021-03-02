package com.example.foodfinder.ui.discover

import android.location.Location
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodfinder.Constants
import com.example.foodfinder.Place
import com.example.foodfinder.database.getDatabase
import com.example.foodfinder.network.PlacesApiService
import com.example.foodfinder.repository.PlacesDataSource
import com.example.foodfinder.repository.PlacesRepository
import kotlinx.coroutines.launch
import java.lang.Exception

class DiscoverViewModel(val apiService: PlacesApiService, dataSource: PlacesDataSource) : ViewModel() {

    val placeList = MutableLiveData<List<Place>>()

    fun findNearByRestaurant(currentLocation: Location) {
        viewModelScope.launch {
            try {
                val place = apiService.getNearByLocation(locationToString(currentLocation),2000, "restaurant", Constants.API_KEY)
                placeList.value = place.results
            } catch (e :Exception){
                Log.e("Error", e.toString())
            }
        }
    }

    private fun locationToString(location: Location) : String {
        return location.latitude.toString() + "," + location.longitude.toString()
    }






}