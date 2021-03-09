package com.example.foodfinder.ui.detail

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.foodfinder.Constants
import com.example.foodfinder.network.model.Place
import com.example.foodfinder.network.model.PlaceDetail
import com.example.foodfinder.database.getLikeDatabase
import com.example.foodfinder.network.PlacesApi
import com.example.foodfinder.repository.LikedRepository
import com.example.foodfinder.ui.discover.PlaceApiStatus
import kotlinx.coroutines.launch
import java.lang.Exception

class RestaurantDetailViewModel(place: Place, application: Application) : AndroidViewModel(application){

    private val database = getLikeDatabase(application)
    private val likedRepository = LikedRepository(database.likedDatabaseDao)

    private val _placeDetail = MutableLiveData<PlaceDetail>()
    val placeDetail : LiveData<PlaceDetail>
        get() = _placeDetail

    private val _photoApiLink = MutableLiveData<String>()
    val photoApiLink : LiveData<String>
        get() = _photoApiLink

    // The internal MutableLiveData that stores the status of the most recent request
    private val _status = MutableLiveData<PlaceApiStatus>()

    // The external immutable LiveData for the request status
    val status: LiveData<PlaceApiStatus>
        get() = _status

    init {
        getPlaceDetail(place)
    }

    private fun getPlaceDetail(place: Place){
        viewModelScope.launch {
            try {
                _status.value = PlaceApiStatus.LOADING
                _placeDetail.value = PlacesApi.retrofitService.getPlaceDetail(place.place_id,"name, photo, place_id, vicinity",  Constants.API_KEY).result
                if (_placeDetail.value!!.photos.isNotEmpty()){
                    val photoRef =  _placeDetail.value!!.photos[0].photo_reference
                    _photoApiLink.value = buildPhotoAPI(photoRef)
                }

                _status.value = PlaceApiStatus.DONE
            } catch (e : Exception){
                Log.i("Error", e.toString())
                _status.value = PlaceApiStatus.ERROR
            }
        }
    }

    fun insertLikedPlace(place : Place){
        viewModelScope.launch {
            try {
                likedRepository.insertToDatabase(place)
            } catch (e : Exception){
                Log.i("Error", e.toString())
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