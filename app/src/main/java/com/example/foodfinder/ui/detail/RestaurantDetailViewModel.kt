package com.example.foodfinder.ui.detail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.foodfinder.Place

class RestaurantDetailViewModel(place: Place, application: Application) : AndroidViewModel(application){
    private val _selectedPlace= MutableLiveData<Place>()

    val selectedPlace: LiveData<Place>
        get() = _selectedPlace

    init {
        _selectedPlace.value = place
    }



}