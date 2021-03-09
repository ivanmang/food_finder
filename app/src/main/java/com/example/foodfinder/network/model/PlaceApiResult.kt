package com.example.foodfinder.network.model

data class PlaceApiResult (
    val html_attributions : List<String>,
    val result: PlaceDetail,
    val status : String
)