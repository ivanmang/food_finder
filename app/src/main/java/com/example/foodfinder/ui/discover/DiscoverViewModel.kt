package com.example.foodfinder.ui.discover

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DiscoverViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text

    private val _lastlocation = MutableLiveData<Location>()
    var lastlocation : LiveData<Location> = _lastlocation
}