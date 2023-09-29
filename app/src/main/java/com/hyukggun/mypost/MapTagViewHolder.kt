package com.hyukggun.mypost

import androidx.recyclerview.widget.RecyclerView
import com.hyukggun.mypost.databinding.MapTagItemBinding

class MapTagViewHolder(val binding: MapTagItemBinding): RecyclerView.ViewHolder(binding.root) {
    fun configure(tag: String) {
        binding.tvTag.text = tag
    }
}