package com.example.foodfinder

import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodfinder.network.model.Place
import com.example.foodfinder.ui.browse.RestaurantListAdapter
import com.example.foodfinder.ui.discover.PlaceApiStatus
import com.example.foodfinder.ui.liked_restaurant.LikedRestaurantListAdapter

@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView, data: List<Place>?) {
    val adapter = recyclerView.adapter as RestaurantListAdapter
    adapter.submitList(data)
}

@BindingAdapter("listLikedData")
fun bindLikedRecyclerView(recyclerView: RecyclerView, data: List<Place>?) {
    val adapter = recyclerView.adapter as LikedRestaurantListAdapter
    adapter.submitList(data)
}

@BindingAdapter("imgApiCAll")
fun bindImage(imgView: ImageView, imgApiCAll: String?) {
    imgApiCAll?.let {
        Glide.with(imgView.context)
            .load(it)
            .error(R.drawable.alert_circle)
            .into(imgView)
    }
}

@BindingAdapter("ApiStatus")
fun bindStatus(progressBar: ProgressBar, status: PlaceApiStatus?) {
    when (status) {
        PlaceApiStatus.LOADING -> {
            progressBar.visibility = View.VISIBLE
        }
        PlaceApiStatus.DONE -> {
            progressBar.visibility = View.GONE
        }
    }
}