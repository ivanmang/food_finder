package com.example.foodfinder


data class PlaceDetail (
    val name : String,
    val photos : List<Photo> = ArrayList<Photo>(),
    val geometry : Geometry,
    val vicinity: String = ""
)