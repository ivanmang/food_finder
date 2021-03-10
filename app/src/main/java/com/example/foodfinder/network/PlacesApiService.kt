package com.example.foodfinder.network

import com.example.foodfinder.Constants
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private val retrofit = Retrofit.Builder()
        .addConverterFactory(ScalarsConverterFactory.create())
        .baseUrl(Constants.BASE_URL)
        .build()

interface PlacesApiService {
    @GET("place/nearbysearch/json")
    suspend fun getNearByLocation(@Query("location") location: String, @Query("radius") radius: Int, @Query("type") type:String, @Query("key") apiKey: String) : String

}

object PlacesApi {
    val retrofitService : PlacesApiService by lazy { retrofit.create(PlacesApiService::class.java)}

}