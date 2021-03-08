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
import com.example.foodfinder.database.getLikeDatabase
import com.example.foodfinder.network.PlacesApi
import com.example.foodfinder.repository.PlacesRepository
import com.example.foodfinder.ui.discover.PlaceApiStatus
import kotlinx.coroutines.launch
import java.lang.Exception

class BrowseViewModel(application: Application) : ViewModel() {

    private val database = getDatabase(application)
    private val restaurantRepository = PlacesRepository(database.placesDatabaseDao)

    val restaurantList : LiveData<List<Place>> = database.placesDatabaseDao.getAllPlaces()


    // The internal MutableLiveData that stores the status of the most recent request
    private val _status = MutableLiveData<PlaceApiStatus>()

    // The external immutable LiveData for the request status
    val status: LiveData<PlaceApiStatus>
        get() = _status

    private val _navigateToSelectedProperty = MutableLiveData<Place>()

    val navigateToSelectedProperty: LiveData<Place>
        get() = _navigateToSelectedProperty
    
    fun getNearbyRestaurant(location: Location){
        viewModelScope.launch {
            try {
                _status.value = PlaceApiStatus.LOADING
                val response = PlacesApi.retrofitService.getNearByLocation(locationToString(location), 100, "restaurant", Constants.API_KEY ).results
                restaurantRepository.insertToDatabase(response)
                _status.value = PlaceApiStatus.DONE
            } catch (e :Exception) {
                Log.i("Error", e.toString())
                _status.value = PlaceApiStatus.ERROR
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