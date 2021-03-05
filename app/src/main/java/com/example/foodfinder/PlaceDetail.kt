package com.example.foodfinder


data class PlaceDetail (
    val name : String,
    val rating : String = "",
    val formatted_phone_number : String = "",
    val photos : List<Photo> = ArrayList<Photo>(),
    val geometry : Geometry
)