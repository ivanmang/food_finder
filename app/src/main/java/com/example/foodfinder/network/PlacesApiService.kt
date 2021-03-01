package com.example.foodfinder.network

// import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.example.foodfinder.ApiResult
import com.example.foodfinder.Constants
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
// import kotlinx.coroutines.Deferred
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
}

object PlacesApi {
    val retrofitService : PlacesApiService by lazy { retrofit.create(PlacesApiService::class.java)}
}