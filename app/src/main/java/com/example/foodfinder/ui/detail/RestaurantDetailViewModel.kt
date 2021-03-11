package com.example.foodfinder.ui.detail

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.foodfinder.Constants
import com.example.foodfinder.network.model.Place
import com.example.foodfinder.database.getLikeDatabase
import com.example.foodfinder.repository.LikedRepository
import com.example.foodfinder.ui.discover.PlaceApiStatus
import kotlinx.coroutines.launch
import java.lang.Exception

class RestaurantDetailViewModel(place: Place, application: Application) : AndroidViewModel(application){

    private val likedDatabase = getLikeDatabase(application)
    private val likedRepository = LikedRepository(likedDatabase.likedDatabaseDao)

    private val _selectedPlace = MutableLiveData<Place>()
    val selectedPlace : LiveData<Place>
        get() = _selectedPlace

    private val _photoApiLink = MutableLiveData<String>()
    val photoApiLink : LiveData<String>
        get() = _photoApiLink

    // The internal MutableLiveData that stores the status of the most recent request
    private val _status = MutableLiveData<PlaceApiStatus>()

    // The external immutable LiveData for the request status
    val status: LiveData<PlaceApiStatus>
        get() = _status

    init {
        initializePlace(place)
    }

    private fun initializePlace(place: Place) {
        viewModelScope.launch {
            try {
                _status.value = PlaceApiStatus.LOADING
                _selectedPlace.value = place
                if (place.photoRef.isNotEmpty()){
                    _photoApiLink.value = buildPhotoAPI(place.photoRef)
                }
                _status.value = PlaceApiStatus.DONE
            }catch (e : Exception){
                Log.i("Error", e.toString())
                _status.value = PlaceApiStatus.ERROR
            }
        }
    }


    fun insertLikedPlace(place : Place){
        viewModelScope.launch {
            try {
                _status.value = PlaceApiStatus.LOADING
                likedRepository.insertToDatabase(place)
                _status.value = PlaceApiStatus.DONE
            } catch (e : Exception){
                Log.i("Error", e.toString())
                _status.value = PlaceApiStatus.ERROR
            }
        }
    }

    private fun buildPhotoAPI(photoRef: String): String {
        return Constants.BASE_URL + "place/photo?" +
                "maxwidth=400" +
                "&photoreference=" +photoRef +
                "&key="+Constants.API_KEY
    }

}