package com.example.foodfinder.ui.dashboard

import android.app.Application
import android.location.Location
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodfinder.ApiResult
import com.example.foodfinder.Constants
import com.example.foodfinder.database.getDatabase
import com.example.foodfinder.network.PlacesApi
import com.example.foodfinder.repository.PlacesRepository
import kotlinx.coroutines.launch
import java.lang.Exception

class BrowseViewModel(application: Application) : ViewModel() {

    private val _result = MutableLiveData<ApiResult>()

    val result : LiveData<ApiResult>
        get() = _result


    private val database = getDatabase(application)

    
    fun getNearbyRestaurant(location: Location){
        viewModelScope.launch {
            try {
                _result.value = PlacesApi.retrofitService.getNearByLocation(locationToString(location), 1500, "restaurant", Constants.API_KEY )
                Log.i("Result: ", _result.value.toString())
            } catch (e :Exception) {
                Log.i("Error", e.toString())
            }
        }
    }

    private fun locationToString(location: Location) : String {
        return location.latitude.toString() + "," + location.longitude.toString()
    }

}