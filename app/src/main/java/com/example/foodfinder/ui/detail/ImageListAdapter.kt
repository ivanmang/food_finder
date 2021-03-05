package com.example.foodfinder.ui.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.foodfinder.ImageApiString
import com.example.foodfinder.databinding.ImageViewItemBinding

class ImageListAdapter: ListAdapter<ImageApiString, ImageListAdapter.ImageViewHolder>(
    DiffCallback
) {
    class ImageViewHolder(private var binding : ImageViewItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(image : ImageApiString){
            binding.imageApiString = image
            binding.executePendingBindings()

        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<ImageApiString>() {
        override fun areItemsTheSame(oldItem: ImageApiString, newItem: ImageApiString): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: ImageApiString, newItem: ImageApiString): Boolean {
            return oldItem.apiString == newItem.apiString
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        return ImageViewHolder(ImageViewItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val image = getItem(position)
        holder.bind(image)
    }

}