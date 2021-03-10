package com.example.foodfinder.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.foodfinder.databinding.FragmentDetailBinding
import com.example.foodfinder.ui.discover.DiscoverViewModel
import com.google.android.material.appbar.AppBarLayout

class RestaurantDetailFragment : Fragment() {

    private lateinit var restaurantDetailViewModel: RestaurantDetailViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val application = requireNotNull(activity).application
        val place = RestaurantDetailFragmentArgs.fromBundle(requireArguments()).selectedPlace
        val viewModelFactory = RestaurantViewModelFactory(place, application)
        val binding = FragmentDetailBinding.inflate(inflater)
        binding.lifecycleOwner = this

        restaurantDetailViewModel = ViewModelProvider(this, viewModelFactory).get(RestaurantDetailViewModel::class.java)
        binding.viewModel = restaurantDetailViewModel


        binding.likeButton.setOnClickListener {
            restaurantDetailViewModel.insertLikedPlace(place)
            this.findNavController().navigate(RestaurantDetailFragmentDirections.actionRestaurantDetailFragmentToNavigationVisited())
        }

        val listener = AppBarLayout.OnOffsetChangedListener { unused, verticalOffset ->
            val seekPosition = -verticalOffset / binding.appbarLayout.totalScrollRange.toFloat()
            binding.motionLayout.progress = seekPosition
        }

        binding.appbarLayout.addOnOffsetChangedListener(listener)

        return binding.root
    }
}