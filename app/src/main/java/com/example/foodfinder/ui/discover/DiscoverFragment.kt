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
import com.google.android.gms.maps.model.Marker
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
    private var currentMarker : Marker? = null


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
            updateCamera()
        }
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        return binding.root
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
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
                    map.clear()
                    addMarkerToNearByRestaurant()
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(discoverViewModel.lastLocation.value!!.latitude, discoverViewModel.lastLocation.value!!.longitude), ZOOM_LEVEL))
                } else {
                    val defaultLocation = LatLng(37.422160, -122.084270)
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, ZOOM_LEVEL))
                }
            }
        } else {
            enableMyLocation()
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
            map.addMarker(MarkerOptions().title(it.name).position(LatLng(lat, long)))
        })
        map.setOnMarkerClickListener(GoogleMap.OnMarkerClickListener {
            marker ->
            marker.showInfoWindow()
            currentMarker = marker
            Log.i("hi1",marker.title)
            Log.i("hi7", discoverViewModel.restaurantPlace.value?.name.toString())
            return@OnMarkerClickListener true
        })
        map.setOnInfoWindowClickListener {
            Log.i("hi2", currentMarker!!.title)
            //discoverViewModel.getPlaceFromName(marker.title)
            Log.i("hi3", discoverViewModel.restaurantPlace.value?.name.toString())
            /*discoverViewModel.restaurantPlace.observe(viewLifecycleOwner, Observer { place ->
                Log.i("hi4", discoverViewModel.restaurantPlace.value?.name.toString())
                this.findNavController().navigate(DiscoverFragmentDirections.actionNavigationHomeToRestaurantDetailFragment(place))
                Log.i("hi5", discoverViewModel.restaurantPlace.value?.name.toString())
            })*/
            Log.i("hi6", discoverViewModel.restaurantPlace.value?.name.toString())
        }
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