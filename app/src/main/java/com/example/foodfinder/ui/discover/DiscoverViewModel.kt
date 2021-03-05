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

class DiscoverViewModel(application: Application) : ViewModel() {
    private val database = getDatabase(application)
    private val restaurantRepository = PlacesRepository(database.placesDatabaseDao)

    private val _restaurantDetail = MutableLiveData<PlaceDetail>()

    val restaurantDetail : LiveData<PlaceDetail>
        get() = _restaurantDetail

    val restaurantList : LiveData<List<Place>> = database.placesDatabaseDao.getAllPlaces()

    fun getNearbyRestaurant(location: Location){
        viewModelScope.launch {
            try {
                val response = PlacesApi.retrofitService.getNearByLocation(locationToString(location), 1500, "restaurant", Constants.API_KEY ).results
                restaurantRepository.insertToDatabase(response)
                Log.i("value", response.toString())
            } catch (e :Exception) {
                Log.i("Error", e.toString())
            }
        }
    }

    private fun locationToString(location: Location) : String {
        return location.latitude.toString() + "," + location.longitude.toString()
    }

    fun getNearByRestaurantDetail(place: Place?) {
        viewModelScope.launch {
            try {
                _restaurantDetail.value = PlacesApi.retrofitService.getPlaceDetail(place!!.place_id, Constants.API_KEY).result
                //Log.i("value", response.toString())
            } catch (e :Exception) {
                Log.i("Error", e.toString())
            }
        }
    }

}