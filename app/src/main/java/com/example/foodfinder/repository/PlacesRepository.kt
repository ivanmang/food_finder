package com.example.foodfinder.repository

import android.location.Location
import androidx.lifecycle.MutableLiveData
import com.example.foodfinder.Place
import com.example.foodfinder.database.PlacesDatabaseDao
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PlacesRepository(private val database: PlacesDatabaseDao ,
private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO) : PlacesDataSource{


    override suspend fun getPlaces(): List<Place> = withContext(ioDispatcher){
        return@withContext database.getAllPlaces()
    }


}