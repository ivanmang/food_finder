package com.example.foodfinder.network.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "place_table")
data class Place(
    @PrimaryKey val place_id: String,
    val name: String,
    val lat: Double,
    val lng: Double,
    val vicinity: String,
    val photoRef : String
) : Parcelable