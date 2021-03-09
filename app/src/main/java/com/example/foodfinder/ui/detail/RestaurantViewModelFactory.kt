package com.example.foodfinder.ui.detail

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.foodfinder.network.model.Place

class RestaurantViewModelFactory(private val place: Place,
                                 private val application: Application) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RestaurantDetailViewModel::class.java)) {
            return RestaurantDetailViewModel(place, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}