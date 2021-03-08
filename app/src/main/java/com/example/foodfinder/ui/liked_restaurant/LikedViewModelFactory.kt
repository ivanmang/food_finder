package com.example.foodfinder.ui.liked_restaurant

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.foodfinder.ui.browse.BrowseViewModel

class LikedViewModelFactory (
    private val application: Application
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LikedViewModel::class.java)) {
            return LikedViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}