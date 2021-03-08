package com.example.foodfinder.ui.liked_restaurant

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.foodfinder.R
import com.example.foodfinder.databinding.FragmentDiscoverBinding
import com.example.foodfinder.databinding.FragmentVisitedBinding
import com.example.foodfinder.ui.discover.DiscoverViewModel
import com.example.foodfinder.ui.discover.DiscoverViewModelFactory

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
        val root = inflater.inflate(R.layout.fragment_visited, container, false)
        return root
    }
}