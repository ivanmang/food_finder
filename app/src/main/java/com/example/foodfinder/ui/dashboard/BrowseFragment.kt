package com.example.foodfinder.ui.dashboard

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.foodfinder.R
import com.example.foodfinder.databinding.FragmentBrowseBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng

class BrowseFragment : Fragment() {

    private lateinit var binding : FragmentBrowseBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private val browseViewModel: BrowseViewModel by lazy {
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onViewCreated()"
        }
        ViewModelProvider(this, BrowseViewModelFactory(activity.application)).get(BrowseViewModel::class.java)

    }


    @SuppressLint("MissingPermission")
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_browse, container, false)
        binding.viewModel = browseViewModel
        binding.lifecycleOwner = this

        binding.currentLocationButton.setOnClickListener {
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity as Activity)
            if (isForegroundLocationGranted()) {
                val locationResult = fusedLocationClient.lastLocation
                locationResult.addOnCompleteListener { task ->
                    if (task.isSuccessful && task.result != null) {
                        val lastLocation = task.result
                        browseViewModel.getNearbyRestaurant(lastLocation)
                    }
                }
            }
        }

        return binding.root
    }

    private fun isForegroundLocationGranted(): Boolean {
        return PackageManager.PERMISSION_GRANTED ==
                ActivityCompat.checkSelfPermission(requireContext(),
                        Manifest.permission.ACCESS_FINE_LOCATION)
    }
}