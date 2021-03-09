package com.example.foodfinder.repository

import com.example.foodfinder.network.model.Place
import com.example.foodfinder.database.PlacesDatabaseDao
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PlacesRepository(private val database: PlacesDatabaseDao,
private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO) {

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


}