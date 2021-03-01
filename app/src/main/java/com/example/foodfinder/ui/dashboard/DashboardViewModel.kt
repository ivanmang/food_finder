package com.example.foodfinder.ui.dashboard

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodfinder.ApiResult
import com.example.foodfinder.Constants
import com.example.foodfinder.Place
import com.example.foodfinder.network.PlacesApi
import kotlinx.coroutines.launch
import java.lang.Exception

class DashboardViewModel : ViewModel() {

    private val _result = MutableLiveData<ApiResult>()

    val result : LiveData<ApiResult>
        get() = _result

    private val _location = MutableLiveData<String>()

    val location: LiveData<String>
        get() = _location

    private fun getPlaces(){
        viewModelScope.launch {
            try {
                _result.value = PlacesApi.retrofitService.getNearByLocation("-33.8670522,151.1957362", 1500, "restaurant", Constants.API_KEY )
                Log.i("Result: ", _result.value.toString())
            } catch (e :Exception) {
                Log.i("Error", e.toString())
            }
        }
    }

    init {
        getPlaces()
    }
}