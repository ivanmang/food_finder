package com.example.foodfinder


data class PlaceDetail (
    val name : String = "",
    val rating : String = "",
    val photos : List<Photo> = ArrayList()
)