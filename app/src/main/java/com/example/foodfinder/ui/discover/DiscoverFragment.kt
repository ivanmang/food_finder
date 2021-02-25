package com.example.foodfinder.ui.discover

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.foodfinder.R
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.snackbar.Snackbar

class DiscoverFragment : Fragment(), OnMapReadyCallback {

    private lateinit var discoverViewModel: DiscoverViewModel
    private lateinit var map: GoogleMap
    private val ZOOM_LEVEL = 18f
    private val REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE = 111

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        discoverViewModel =
                ViewModelProvider(this).get(DiscoverViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_discover, container, false)

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        return root
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
                    val lastLocation = task.result!!
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(lastLocation.latitude, lastLocation.longitude), ZOOM_LEVEL))
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