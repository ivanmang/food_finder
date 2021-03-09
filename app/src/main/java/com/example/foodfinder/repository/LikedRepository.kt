package com.example.foodfinder.repository

import com.example.foodfinder.network.model.Place
import com.example.foodfinder.database.LikedDatabaseDao
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LikedRepository(private val database: LikedDatabaseDao,
                       private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO){

    suspend fun insertToDatabase(place : Place) = withContext(ioDispatcher){
        database.insert(place)
    }

    suspend fun clearDatabase(){
        database.deleteAll()
    }


}