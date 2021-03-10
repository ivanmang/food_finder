package com.example.foodfinder.ui.discover

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.foodfinder.R
import com.example.foodfinder.databinding.FragmentDiscoverBinding
import com.example.foodfinder.network.model.Place
import com.example.foodfinder.ui.browse.BrowseViewModel
import com.example.foodfinder.ui.browse.BrowseViewModelFactory
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.util.*

class DiscoverFragment() : Fragment(), OnMapReadyCallback {

    private lateinit var map: GoogleMap
    private val ZOOM_LEVEL = 18f
    private val REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE = 111
    private lateinit var binding : FragmentDiscoverBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private val discoverViewModel: DiscoverViewModel by lazy {
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onViewCreated()"
        }
        ViewModelProvider(this, DiscoverViewModelFactory(activity.application )).get(DiscoverViewModel::class.java)

    }


    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_discover, container, false)
        binding.viewModel = discoverViewModel
        binding.lifecycleOwner = this
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        addMarkerToNearByRestaurant()
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.refresh_action_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    /**
     * Updates the filter in the [OverviewViewModel] when the menu items are selected from the
     * overflow menu.
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        updateCamera()
        updateNearByRestaurant()
        map.clear()
        return true
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        updateCamera()
        enableMyLocation()
    }

    @SuppressLint("MissingPermission")
    private fun updateCamera() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity as Activity)
        if (isForegroundLocationGranted()) {
            val locationResult = fusedLocationClient.lastLocation
            locationResult.addOnCompleteListener { task ->
                if (task.isSuccessful && task.result != null) {
                    val result = task.result
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(result.latitude, result.longitude), ZOOM_LEVEL))
                } else {
                    val defaultLocation = LatLng(37.422160, -122.084270)
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, ZOOM_LEVEL))
                }
            }
        } /*else {
            enableMyLocation()
        }*/
    }

    private fun isForegroundLocationGranted(): Boolean {
        return PackageManager.PERMISSION_GRANTED ==
                ActivityCompat.checkSelfPermission(requireContext(),
                        Manifest.permission.ACCESS_FINE_LOCATION)
    }

    @SuppressLint("MissingPermission")
    private fun updateNearByRestaurant(){
        discoverViewModel.clearDatabase()
        val locationResult = fusedLocationClient.lastLocation
        locationResult.addOnCompleteListener { task ->
            if (task.isSuccessful && task.result != null) {
                val result = task.result
                discoverViewModel.getNearbyRestaurant(result)
                }
        }
    }

    private fun addMarkerToNearByRestaurant(){
        discoverViewModel.mapRestaurantList.observe(viewLifecycleOwner, Observer {
            for (place in it) {
                val newMarker = map.addMarker(MarkerOptions().title(place.name).position(LatLng(place.lat, place.lng)))
                newMarker.tag = place
            }

            map.setOnMarkerClickListener(GoogleMap.OnMarkerClickListener {
                    marker ->
                marker.showInfoWindow()
                return@OnMarkerClickListener true
            })
            map.setOnInfoWindowClickListener { info ->
                this.findNavController().navigate(DiscoverFragmentDirections.actionNavigationHomeToRestaurantDetailFragment(
                    info.tag as Place
                ))
            }
        })
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