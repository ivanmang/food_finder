package com.example.foodfinder.ui.liked_restaurant

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.foodfinder.Place
import com.example.foodfinder.database.getDatabase
import com.example.foodfinder.database.getLikeDatabase
import com.example.foodfinder.repository.PlacesRepository
import com.example.foodfinder.ui.discover.DiscoverViewModel

class LikedViewModel(application: Application) : ViewModel() {

    private val database = getLikeDatabase(application)
    private lateinit var likedViewModel: LikedViewModel

    val likedRestaurantList : LiveData<List<Place>> = database.likedDatabaseDao.getAllPlaces()

}