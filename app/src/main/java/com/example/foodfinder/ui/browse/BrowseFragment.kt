package com.example.foodfinder.ui.browse

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
import com.example.foodfinder.R
import com.example.foodfinder.databinding.FragmentBrowseBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class BrowseFragment : Fragment() {

    private lateinit var binding : FragmentBrowseBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var lastLocation : Location

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

        binding.restaurantList.adapter = RestaurantListAdapter(RestaurantListAdapter.OnClickListener {
            browseViewModel.displayPropertyDetails(it)
        })

        binding.currentLocationButton.setOnClickListener {
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity as Activity)
            if (isForegroundLocationGranted()) {
                val locationResult = fusedLocationClient.lastLocation
                locationResult.addOnCompleteListener { task ->
                    if (task.isSuccessful && task.result != null) {
                        lastLocation = task.result
                        browseViewModel.clearDatabase()
                        browseViewModel.getNearbyRestaurant(lastLocation)
                        Log.i("Restaurant :", "Success")
                    }
                }
            }
        }

        browseViewModel.navigateToSelectedProperty.observe(viewLifecycleOwner, Observer {
            if ( null != it ) {
                // Must find the NavController from the Fragment
                this.findNavController().navigate( BrowseFragmentDirections.actionNavigationBrowseToRestaurantDetailFragment(it))
                // Tell the ViewModel we've made the navigate call to prevent multiple navigation
                browseViewModel.displayPropertyDetailsComplete()
            }
        })


        return binding.root
    }



    private fun isForegroundLocationGranted(): Boolean {
        return PackageManager.PERMISSION_GRANTED ==
                ActivityCompat.checkSelfPermission(requireContext(),
                        Manifest.permission.ACCESS_FINE_LOCATION)
    }
}