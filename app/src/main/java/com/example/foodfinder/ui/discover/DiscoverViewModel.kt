package com.example.foodfinder.ui.discover

import android.app.Application
import android.location.Location
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodfinder.Constants
import com.example.foodfinder.Place
import com.example.foodfinder.PlaceApiResult
import com.example.foodfinder.PlaceDetail
import com.example.foodfinder.database.getDatabase
import com.example.foodfinder.network.PlacesApi
import com.example.foodfinder.network.PlacesApiService
import com.example.foodfinder.repository.PlacesDataSource
import com.example.foodfinder.repository.PlacesRepository
import kotlinx.coroutines.launch
import java.lang.Exception

enum class PlaceApiStatus {LOADING, ERROR, DONE}

class DiscoverViewModel(application: Application) : ViewModel() {
    private val database = getDatabase(application)
    private val restaurantRepository = PlacesRepository(database.placesDatabaseDao)

    private val _restaurantDetail = MutableLiveData<PlaceDetail>()

    val restaurantDetail : LiveData<PlaceDetail>
        get() = _restaurantDetail

    private val _restaurantPlace = MutableLiveData<Place>()

    val restaurantPlace : LiveData<Place>
        get() = _restaurantPlace

    private val _lastLocation = MutableLiveData<Location>()

    val lastLocation : LiveData<Location>
        get() = _lastLocation

    // The internal MutableLiveData that stores the status of the most recent request
    private val _status = MutableLiveData<PlaceApiStatus>()

    // The external immutable LiveData for the request status
    val status: LiveData<PlaceApiStatus>
        get() = _status

    val restaurantList : LiveData<List<Place>> = database.placesDatabaseDao.getAllPlaces()

    fun getNearbyRestaurant(location: Location){
        viewModelScope.launch {
            _status.value = PlaceApiStatus.LOADING
            try {
                restaurantRepository.clearDatabase()
                val response = PlacesApi.retrofitService.getNearByLocation(locationToString(location), 100, "restaurant", Constants.API_KEY ).results
                restaurantRepository.insertToDatabase(response)
                _status.value = PlaceApiStatus.DONE
                Log.i("value", response.toString())
            } catch (e :Exception) {
                _status.value = PlaceApiStatus.ERROR
                Log.i("Error", e.toString())
            }
        }
    }

    private fun locationToString(location: Location) : String {
        return location.latitude.toString() + "," + location.longitude.toString()
    }

    fun getNearByRestaurantDetail(place: Place?) {
        viewModelScope.launch {
            _status.value = PlaceApiStatus.LOADING
            try {
                _restaurantDetail.value = PlacesApi.retrofitService.getPlaceDetail(place!!.place_id, "name, photo, place_id, vicinity", Constants.API_KEY).result
                _status.value = PlaceApiStatus.DONE
                //Log.i("value", response.toString())
            } catch (e :Exception) {
                _status.value = PlaceApiStatus.ERROR
                Log.i("Error", e.toString())
            }
        }
    }

    fun getPlaceFromName(title: String?){
        viewModelScope.launch {
            _status.value = PlaceApiStatus.LOADING
            try {
                if (title != null) {
                    _restaurantPlace.value = restaurantRepository.findPlaceByName(title)
                    Log.i("Name", title)
                    _status.value = PlaceApiStatus.DONE
                }
            } catch (e : Exception){
                _status.value = PlaceApiStatus.ERROR
                Log.i("Error", e.toString())
            }
        }

    }

    fun setLastLocation(lastLocation : Location){
        _lastLocation.value = lastLocation
    }

}