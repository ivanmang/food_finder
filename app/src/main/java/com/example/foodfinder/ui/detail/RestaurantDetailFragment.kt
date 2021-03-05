package com.example.foodfinder.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.foodfinder.databinding.FragmentDetailBinding

class RestaurantDetailFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val application = requireNotNull(activity).application
        val binding = FragmentDetailBinding.inflate(inflater)
        binding.lifecycleOwner = this
        val place = RestaurantDetailFragmentArgs.fromBundle(requireArguments()).selectedPlace
        val viewModelFactory = RestaurantViewModelFactory(place, application)

        binding.imageList.adapter = ImageListAdapter()

        binding.viewModel = ViewModelProvider(this, viewModelFactory).get(RestaurantDetailViewModel::class.java)

        return binding.root
    }
}