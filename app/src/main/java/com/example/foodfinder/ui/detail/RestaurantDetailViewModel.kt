package com.example.foodfinder.ui.detail

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.foodfinder.Constants
import com.example.foodfinder.Place
import com.example.foodfinder.PlaceDetail
import com.example.foodfinder.network.PlacesApi
import kotlinx.coroutines.launch
import java.lang.Exception

class RestaurantDetailViewModel(place: Place, application: Application) : AndroidViewModel(application){

    private val _placeDetail = MutableLiveData<PlaceDetail>()
    val placeDetail : LiveData<PlaceDetail>
        get() = _placeDetail

    private val _photoApiLink = MutableLiveData<String>()
    val photoApiLink : LiveData<String>
        get() = _photoApiLink

    init {
        getPlaceDetail(place)
    }

    fun getPlaceDetail(place: Place){
        viewModelScope.launch {
            try {
                _placeDetail.value = PlacesApi.retrofitService.getPlaceDetail(place.place_id, Constants.API_KEY).result
                val photoRef =  _placeDetail.value!!.photos.get(0).photo_reference
                _photoApiLink.value = buildPhotoAPI(photoRef)
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