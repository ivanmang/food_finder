package com.example.foodfinder

import android.location.Location
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
@Entity(tableName = "place_table")
data class Place(
    @PrimaryKey val place_id: String,
    val name: String,
    val vicinity: String,
) : Parcelable