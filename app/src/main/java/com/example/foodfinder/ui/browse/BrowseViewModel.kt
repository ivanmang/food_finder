package com.example.foodfinder.ui.browse

import android.app.Application
import android.location.Location
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodfinder.ApiResult
import com.example.foodfinder.Constants
import com.example.foodfinder.Place
import com.example.foodfinder.database.getDatabase
import com.example.foodfinder.network.PlacesApi
import kotlinx.coroutines.launch
import java.lang.Exception

class BrowseViewModel(application: Application) : ViewModel() {


    private val _restaurantList = MutableLiveData<List<Place>>()
    val restaurantList : LiveData<List<Place>>
        get() = _restaurantList

    
    fun getNearbyRestaurant(location: Location){
        viewModelScope.launch {
            try {
                _restaurantList.value = PlacesApi.retrofitService.getNearByLocation(locationToString(location), 1500, "restaurant", Constants.API_KEY ).results
            } catch (e :Exception) {
                _restaurantList.value = ArrayList()
                Log.i("Error", e.toString())
            }
        }
    }

    private fun locationToString(location: Location) : String {
        return location.latitude.toString() + "," + location.longitude.toString()
    }

}