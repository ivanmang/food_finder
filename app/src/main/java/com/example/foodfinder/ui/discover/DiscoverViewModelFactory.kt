package com.example.foodfinder.ui.discover

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.foodfinder.database.getDatabase
import com.example.foodfinder.network.PlacesApi
import com.example.foodfinder.repository.PlacesRepository

class DiscoverViewModelFactory (val context: Context
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DiscoverViewModel::class.java)) {
            return DiscoverViewModel(PlacesApi.retrofitService, PlacesRepository(getDatabase(context).placesDatabaseDao)) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}