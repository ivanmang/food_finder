package com.example.foodfinder.repository

import com.example.foodfinder.Place

interface PlacesDataSource {
    suspend fun getPlaces() : List<Place>
}
