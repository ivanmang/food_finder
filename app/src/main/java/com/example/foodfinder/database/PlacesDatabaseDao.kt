package com.example.foodfinder.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.foodfinder.network.model.Place

@Dao
interface PlacesDatabaseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(place: Place)


    @Query("SELECT * FROM place_table")
    fun getAllPlaces(): LiveData<List<Place>>

    @Query("SELECT * FROM place_table WHERE place_id = :placeId")
    fun getPlaceByPlaceId(placeId : String): LiveData<Place>

    @Query("DELETE FROM place_table")
    suspend fun deleteAll()
}

@Database(entities = [Place::class], version = 1, exportSchema = false)
abstract class PlacesDatabase : RoomDatabase() {
    abstract val placesDatabaseDao : PlacesDatabaseDao
}

private lateinit var INSTANCE : PlacesDatabase

fun getDatabase(context: Context): PlacesDatabase {
    synchronized(PlacesDatabase::class.java) {
        INSTANCE = Room.databaseBuilder(context.applicationContext,
        PlacesDatabase::class.java, "places").build()
    }
    return INSTANCE
}