package com.aoztg.greengrim.presentation.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aoztg.greengrim.databinding.ItemHomeHotNftBinding
import com.aoztg.greengrim.presentation.ui.home.model.HotNft
import com.aoztg.greengrim.presentation.util.DefaultDiffUtil

class HotNftAdapter : ListAdapter<HotNft, HotNftViewHolder>(DefaultDiffUtil<HotNft>()) {

    override fun onBindViewHolder(holder: HotNftViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HotNftViewHolder {
        return HotNftViewHolder(
            ItemHomeHotNftBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }
}

class HotNftViewHolder(private val binding: ItemHomeHotNftBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: HotNft) {
        binding.item = item
    }
}