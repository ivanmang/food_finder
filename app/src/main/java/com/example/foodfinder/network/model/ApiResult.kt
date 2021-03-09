package com.example.foodfinder.network.model

data class ApiResult (
    val html_attributions : List<String>,
    val results: List<Place>,
    val status : String
)