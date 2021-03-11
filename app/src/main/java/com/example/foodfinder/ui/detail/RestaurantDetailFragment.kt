package com.example.foodfinder.ui.detail

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.foodfinder.R
import com.example.foodfinder.databinding.FragmentDetailBinding
import com.example.foodfinder.ui.discover.PlaceApiStatus
import com.google.android.material.appbar.AppBarLayout

class RestaurantDetailFragment : Fragment() {

    private lateinit var restaurantDetailViewModel: RestaurantDetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val application = requireNotNull(activity).application
        val place = RestaurantDetailFragmentArgs.fromBundle(requireArguments()).selectedPlace
        val viewModelFactory = RestaurantViewModelFactory(place, application)
        val binding = FragmentDetailBinding.inflate(inflater)
        binding.lifecycleOwner = this

        restaurantDetailViewModel =
            ViewModelProvider(this, viewModelFactory).get(RestaurantDetailViewModel::class.java)
        binding.viewModel = restaurantDetailViewModel


        binding.likeButton.setOnClickListener {
            restaurantDetailViewModel.insertLikedPlace(place)
            this.findNavController()
                .navigate(RestaurantDetailFragmentDirections.actionRestaurantDetailFragmentToNavigationVisited())
        }

        val listener = AppBarLayout.OnOffsetChangedListener { unused, verticalOffset ->
            val seekPosition = -verticalOffset / binding.appbarLayout.totalScrollRange.toFloat()
            binding.motionLayout.progress = seekPosition
        }

        binding.appbarLayout.addOnOffsetChangedListener(listener)
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.share_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    /**
     * Updates the filter in the [OverviewViewModel] when the menu items are selected from the
     * overflow menu.
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, "Hey! Let's grab some food in " + intentMessageBuilder())
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivity(shareIntent)
        return true
    }

    private fun intentMessageBuilder() : String{
        val place = restaurantDetailViewModel.selectedPlace.value
        if (place != null){
            return place.name + " at " + place.vicinity
        }
        return ""
    }
}