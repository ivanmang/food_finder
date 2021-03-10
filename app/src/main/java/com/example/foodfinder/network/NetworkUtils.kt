package com.example.foodfinder.network

import com.example.foodfinder.network.model.Place
import org.json.JSONObject

fun parsePlacesJsonResult(jsonResult: JSONObject): ArrayList<Place> {

    val placeList = ArrayList<Place>()

    val resultsArray = jsonResult.getJSONArray("results")
    for (i in 0 until resultsArray.length()){
        val placeJson = resultsArray.getJSONObject(i)
        val placeId = placeJson.getString("place_id")
        val name = placeJson.getString("name")
        val geometryJson = placeJson.getJSONObject("geometry")
        val location = geometryJson.getJSONObject("location")
        val lat = location.getDouble("lat")
        val lng = location.getDouble("lng")
        val vicinity = placeJson.getString("vicinity")
        var photoReference = ""
        if (placeJson.has("photos")){
            val photos = placeJson.getJSONArray("photos")
            val firstPhoto = photos.getJSONObject(0)
            photoReference = firstPhoto.getString("photo_reference")
        }

        val place = Place(placeId, name, lat, lng, vicinity, photoReference )
        placeList.add(place)
    }

    return placeList
}