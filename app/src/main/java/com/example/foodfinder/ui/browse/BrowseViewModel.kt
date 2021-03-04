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
import com.example.foodfinder.repository.PlacesRepository
import kotlinx.coroutines.launch
import java.lang.Exception

class BrowseViewModel(application: Application) : ViewModel() {

    private val database = getDatabase(application)
    private val restaurantRepository = PlacesRepository(database.placesDatabaseDao)

    val restaurantList : LiveData<List<Place>> = database.placesDatabaseDao.getAllPlaces()

    private val _navigateToSelectedProperty = MutableLiveData<Place>()

    val navigateToSelectedProperty: LiveData<Place>
        get() = _navigateToSelectedProperty
    
    fun getNearbyRestaurant(location: Location){
        viewModelScope.launch {
            try {
                val response = PlacesApi.retrofitService.getNearByLocation(locationToString(location), 1500, "restaurant", Constants.API_KEY ).results
                restaurantRepository.insertToDatabase(response)
                //Log.i("value", response.toString())
            } catch (e :Exception) {
                Log.i("Error", e.toString())
            }
        }
    }

    private fun locationToString(location: Location) : String {
        return location.latitude.toString() + "," + location.longitude.toString()
    }

    fun displayPropertyDetails(place: Place) {
        _navigateToSelectedProperty.value = place
    }

    fun displayPropertyDetailsComplete() {
        _navigateToSelectedProperty.value = null
    }

    fun clearDatabase(){
        viewModelScope.launch {
            try {
                restaurantRepository.clearDatabase()
            } catch (e : Exception){
                Log.i("Error", e.toString())
            }
        }
    }

}