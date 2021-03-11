package com.example.foodfinder.ui.liked_restaurant

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodfinder.database.getDatabase
import com.example.foodfinder.network.model.Place
import com.example.foodfinder.database.getLikeDatabase
import com.example.foodfinder.repository.LikedRepository
import com.example.foodfinder.repository.PlacesRepository
import kotlinx.coroutines.launch
import java.lang.Exception

class LikedViewModel(application: Application) : ViewModel() {

    private val database = getLikeDatabase(application)
    private val likedRepository = LikedRepository(database.likedDatabaseDao)

    val likedRestaurantList : LiveData<List<Place>> = database.likedDatabaseDao.getAllPlaces()

    fun clearDatabase() {
        viewModelScope.launch {
            try {
                likedRepository.clearDatabase()
            } catch (e : Exception){
                Log.i("Error", e.toString())
            }
        }

    }

}