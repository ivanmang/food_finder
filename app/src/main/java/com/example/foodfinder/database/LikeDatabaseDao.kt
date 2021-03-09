package com.example.foodfinder.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.foodfinder.network.model.Place

@Dao
interface LikedDatabaseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(place: Place)

    @Update
    suspend fun update(place : Place)

    @Query("SELECT * FROM place_table")
    fun getAllPlaces(): LiveData<List<Place>>

    @Query("DELETE FROM place_table")
    suspend fun deleteAll()

    @Query("DELETE FROM place_table WHERE place_id = :placeID")
    suspend fun deleteByID(placeID : String)
}

@Database(entities = [Place::class], version = 1, exportSchema = false)
abstract class LikedDatabase : RoomDatabase() {
    abstract val likedDatabaseDao : LikedDatabaseDao
}

private lateinit var INSTANCE : LikedDatabase

fun getLikeDatabase(context: Context): LikedDatabase {
    synchronized(LikedDatabase::class.java) {
        INSTANCE = Room.databaseBuilder(context.applicationContext,
            LikedDatabase::class.java, "liked").build()
    }
    return INSTANCE
}