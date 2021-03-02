package com.example.foodfinder.ui.dashboard

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class BrowseViewModelFactory (
    private val application: Application
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BrowseViewModel::class.java)) {
            return BrowseViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}