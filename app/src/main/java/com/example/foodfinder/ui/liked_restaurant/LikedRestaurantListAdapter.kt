package com.example.foodfinder.ui.liked_restaurant

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.foodfinder.network.model.Place
import com.example.foodfinder.databinding.RestaurantViewItemBinding

class LikedRestaurantListAdapter() : ListAdapter<Place, LikedRestaurantListAdapter.PlaceViewHolder>(DiffCallback) {

    class PlaceViewHolder(private var binding : RestaurantViewItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(place : Place){
            binding.place = place
            binding.executePendingBindings()

        }
    }



    companion object DiffCallback : DiffUtil.ItemCallback<Place>() {
        override fun areItemsTheSame(oldItem: Place, newItem: Place): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Place, newItem: Place): Boolean {
            return oldItem.place_id == newItem.place_id
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceViewHolder {
        return PlaceViewHolder(RestaurantViewItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: PlaceViewHolder, position: Int) {
        val place = getItem(position)
        holder.bind(place)
    }


}