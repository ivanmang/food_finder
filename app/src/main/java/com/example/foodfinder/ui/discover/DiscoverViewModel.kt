package com.example.foodfinder.ui.discover

import android.app.Application
import android.location.Location
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodfinder.network.model.Place
import com.example.foodfinder.database.getDatabase
import com.example.foodfinder.repository.PlacesRepository
import kotlinx.coroutines.launch
import java.lang.Exception

enum class PlaceApiStatus {LOADING, ERROR, DONE}

class DiscoverViewModel(application: Application) : ViewModel() {
    private val database = getDatabase(application)
    private val restaurantRepository = PlacesRepository(database.placesDatabaseDao)

    val mapRestaurantList : LiveData<List<Place>> = database.placesDatabaseDao.getAllPlaces()

    // The internal MutableLiveData that stores the status of the most recent request
    private val _status = MutableLiveData<PlaceApiStatus>()

    // The external immutable LiveData for the request status
    val status: LiveData<PlaceApiStatus>
        get() = _status




    fun getNearbyRestaurant(location: Location){
        viewModelScope.launch {
            _status.value = PlaceApiStatus.LOADING
            try {
                restaurantRepository.refreshPlaces(location)
                _status.value = PlaceApiStatus.DONE
            } catch (e :Exception) {
                _status.value = PlaceApiStatus.ERROR
                Log.i("Error", e.toString())
            }
        }
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