package com.example.foodfinder.network

import com.example.foodfinder.ApiResult
import com.example.foodfinder.Constants
import com.example.foodfinder.PlaceApiResult
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

private val retrofit = Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .baseUrl(Constants.BASE_URL)
        .build()

interface PlacesApiService {
    @GET("place/nearbysearch/json")
    suspend fun getNearByLocation(@Query("location") location: String, @Query("radius") radius: Int, @Query("type") type:String, @Query("key") apiKey: String) :ApiResult

    @GET("place/details/json")
    suspend fun getPlaceDetail(@Query("place_id") placeId :String, @Query("field") field : String,  @Query("key") apiKey: String) : PlaceApiResult

}

object PlacesApi {
    val retrofitService : PlacesApiService by lazy { retrofit.create(PlacesApiService::class.java)}

}