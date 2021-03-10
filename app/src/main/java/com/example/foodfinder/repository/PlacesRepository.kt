package com.example.foodfinder.repository

import android.location.Location
import android.util.Log
import com.example.foodfinder.Constants
import com.example.foodfinder.network.model.Place
import com.example.foodfinder.database.PlacesDatabaseDao
import com.example.foodfinder.network.PlacesApi
import com.example.foodfinder.network.parsePlacesJsonResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

class PlacesRepository(private val database: PlacesDatabaseDao,
private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO) {

    suspend fun refreshPlaces(location: Location) = withContext(ioDispatcher){
        val response = PlacesApi.retrofitService.getNearByLocation(locationToString(location), 100, "restaurant", Constants.API_KEY )
        val placesList = parsePlacesJsonResult(JSONObject(response))
        insertToDatabase(placesList)
    }

    private suspend fun insertToDatabase(places : ArrayList<Place>) = withContext(ioDispatcher){
        for(place in places){
            database.insert(place)
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