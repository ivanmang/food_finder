package com.example.foodfinder.ui.browse

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.foodfinder.network.model.Place
import com.example.foodfinder.databinding.RestaurantViewItemBinding

class RestaurantListAdapter(val onClickListener: OnClickListener) : ListAdapter<Place, RestaurantListAdapter.PlaceViewHolder>(DiffCallback) {

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
        holder.itemView.setOnClickListener {
            onClickListener.onClick(place)
        }
        holder.bind(place)
    }



    class OnClickListener(val clickListener: (place : Place) -> Unit) {
        fun onClick(place: Place) = clickListener(place)
    }
}



