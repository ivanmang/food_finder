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

    var placesRankByRating : LiveData<List<Place>> = database.getAllPlaces()

    suspend fun refreshPlaces(location: Location) = withContext(ioDispatcher){
        val response = PlacesApi.retrofitService.getNearByLocation(locationToString(location), 1500, "restaurant", Constants.API_KEY ).results
        insertToDatabase(response)
        placesRankByRating = database.getAllPlaces()
        Log.i("place", placesRankByRating.value?.size.toString())
    }

    suspend fun insertToDatabase(places : List<Place>){
        for(place in places){
            database.insert(place)
            Log.i("Location", place.name)
        }
    }

    suspend fun clearDatabase(){
        database.deleteAll()
    }

    private fun locationToString(location: Location) : String {
        return location.latitude.toString() + "," + location.longitude.toString()
    }


}