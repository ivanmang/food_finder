package com.example.foodfinder

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.foodfinder.ui.browse.RestaurantListAdapter

@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView, data: List<Place>?) {
    val adapter = recyclerView.adapter as RestaurantListAdapter
    adapter.submitList(data)
}