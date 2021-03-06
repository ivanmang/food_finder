package com.example.foodfinder.repository

import android.location.Location
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.foodfinder.Constants
import com.example.foodfinder.Place
import com.example.foodfinder.database.PlacesDatabaseDao
import com.example.foodfinder.network.PlacesApi
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PlacesRepository(private val database: PlacesDatabaseDao ,
private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO) : PlacesDataSource{

    suspend fun insertToDatabase(places : List<Place>) = withContext(ioDispatcher){
        for(place in places){
            database.insert(place)
            //Log.i("Location", place.name)
        }
    }

    suspend fun clearDatabase(){
        database.deleteAll()
    }

    suspend fun findPlaceByName(name : String) = withContext(ioDispatcher){
        return@withContext database.getPlaceByName(name)
    }

    private fun locationToString(location: Location) : String {
        return location.latitude.toString() + "," + location.longitude.toString()
    }


}