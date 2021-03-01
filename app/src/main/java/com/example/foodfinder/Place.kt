package com.example.foodfinder

import android.location.Location
import com.squareup.moshi.Json

data class Place(
                  val name : String,
                  val rating : Double,
                  val vicinity : String
                  )