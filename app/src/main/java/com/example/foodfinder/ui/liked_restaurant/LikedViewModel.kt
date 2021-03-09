package com.example.foodfinder.ui.liked_restaurant

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.foodfinder.network.model.Place
import com.example.foodfinder.database.getLikeDatabase

class LikedViewModel(application: Application) : ViewModel() {

    private val database = getLikeDatabase(application)

    val likedRestaurantList : LiveData<List<Place>> = database.likedDatabaseDao.getAllPlaces()

}