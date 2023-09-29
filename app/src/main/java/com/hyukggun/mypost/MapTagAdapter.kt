package com.hyukggun.mypost

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.hyukggun.mypost.databinding.MapTagItemBinding

class MapTagAdapter(val tags: List<String>): RecyclerView.Adapter<MapTagViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MapTagViewHolder {
        val binding = MapTagItemBinding.inflate(LayoutInflater.from(parent.context))
        return MapTagViewHolder(binding)
    }

    override fun getItemCount() = tags.size

    override fun onBindViewHolder(holder: MapTagViewHolder, position: Int) {
        holder.configure(tags[position])
    }

}