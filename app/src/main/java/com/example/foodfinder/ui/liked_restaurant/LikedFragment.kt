package com.example.foodfinder.ui.liked_restaurant

import android.app.Activity
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.foodfinder.R
import com.example.foodfinder.databinding.FragmentDiscoverBinding
import com.example.foodfinder.databinding.FragmentVisitedBinding
import com.example.foodfinder.ui.discover.DiscoverViewModel
import com.example.foodfinder.ui.discover.DiscoverViewModelFactory
import com.example.foodfinder.ui.discover.PlaceApiStatus
import com.google.android.gms.location.LocationServices

class LikedFragment : Fragment() {

    private lateinit var likedViewModel: LikedViewModel
    private lateinit var binding : FragmentVisitedBinding

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        likedViewModel=
            ViewModelProvider(this, LikedViewModelFactory( requireActivity().application )).get(
                LikedViewModel::class.java)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_visited, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = likedViewModel
        binding.likedRestaurantList.adapter = LikedRestaurantListAdapter()

        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.clear_liked_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        likedViewModel.clearDatabase()
        return true
    }
}