package com.example.foodfinder.ui.discover

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.foodfinder.Constants
import com.example.foodfinder.Place
import com.example.foodfinder.R
import com.example.foodfinder.databinding.FragmentBrowseBinding
import com.example.foodfinder.databinding.FragmentDiscoverBinding
import com.example.foodfinder.network.PlacesApi
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.util.*

class DiscoverFragment() : Fragment(), OnMapReadyCallback {

    private lateinit var discoverViewModel: DiscoverViewModel
    private lateinit var map: GoogleMap
    private val ZOOM_LEVEL = 18f
    private val REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE = 111
    private var lat : Double = 0.0
    private var long : Double = 0.0
    private lateinit var binding : FragmentDiscoverBinding


    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        discoverViewModel =
                ViewModelProvider(this, DiscoverViewModelFactory( requireActivity().application )).get(DiscoverViewModel::class.java)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_discover, container, false)
        binding.lifecycleOwner = this
        binding.getNearbyPlaceButton.setOnClickListener {
            map.clear()
            updateCamera()
        }
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        return binding.root
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        updateCamera()
        enableMyLocation()
    }

    @SuppressLint("MissingPermission")
    private fun updateCamera() {
        val locationProvider = LocationServices.getFusedLocationProviderClient(activity as Activity)
        if (isForegroundLocationGranted()) {
            val locationResult = locationProvider.lastLocation
            locationResult.addOnCompleteListener { task ->
                if (task.isSuccessful && task.result != null) {
                    discoverViewModel.setLastLocation(task.result!!)
                    discoverViewModel.lastLocation.value?.let { it1 ->
                        discoverViewModel.getNearbyRestaurant(
                            it1
                        )
                    }
                    addMarkerToNearByRestaurant()
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(discoverViewModel.lastLocation.value!!.latitude, discoverViewModel.lastLocation.value!!.longitude), ZOOM_LEVEL))
                } else {
                    val defaultLocation = LatLng(37.422160, -122.084270)
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, ZOOM_LEVEL))
                }
            }
        }
    }

    private fun isForegroundLocationGranted(): Boolean {
        return PackageManager.PERMISSION_GRANTED ==
                ActivityCompat.checkSelfPermission(requireContext(),
                        Manifest.permission.ACCESS_FINE_LOCATION)
    }

    private fun addMarkerToNearByRestaurant(){
        discoverViewModel.restaurantList.observe(viewLifecycleOwner, Observer { places ->
            for (place in places){
                discoverViewModel.getNearByRestaurantDetail(place)
            }
        })

        discoverViewModel.restaurantDetail.observe(viewLifecycleOwner, Observer { it ->
            lat = it.geometry.location.lat.toDouble()
            long = it.geometry.location.lng.toDouble()
            val snippet = it.rating
            map.addMarker(MarkerOptions().title(it.name).snippet(snippet).position(LatLng(lat, long)))
        })
        map.setOnMarkerClickListener(GoogleMap.OnMarkerClickListener {
            marker ->
            marker.showInfoWindow()
            discoverViewModel.getPlaceFromName(marker.title)
            discoverViewModel.restaurantPlace.observe(viewLifecycleOwner, Observer { it ->
                this.findNavController().navigate(DiscoverFragmentDirections.actionNavigationHomeToRestaurantDetailFragment(it))
            })
            return@OnMarkerClickListener true
        })
        //Log.i("LIst", list.value?.size.toString())
        //val first = list.value?.get(0)
        //discoverViewModel.getNearByRestaurantDetail(first)
        //val lat = discoverViewModel.restaurantDetail.value?.geometry?.location?.lat
        //val long = discoverViewModel.restaurantDetail.value?.geometry?.location?.lng
    }


    @SuppressLint("MissingPermission")
    private fun enableMyLocation() {
        if (isForegroundLocationGranted()) {
            map.isMyLocationEnabled = true
            Log.i("permitted", "permitted")

        } else {
            val permissionsArray = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)
            requestPermissions(
                    permissionsArray,
                    REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<String>,
            grantResults: IntArray) {
        // Check if location permissions are granted and if so enable the
        // location data layer.
        if ((grantResults.isNotEmpty() &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
            enableMyLocation()
            map.clear()

        }
        updateCamera()
    }


}