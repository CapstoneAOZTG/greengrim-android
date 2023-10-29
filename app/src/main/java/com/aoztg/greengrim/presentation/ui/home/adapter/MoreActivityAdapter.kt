package com.aoztg.greengrim.presentation.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aoztg.greengrim.databinding.ItemHomeMoreActivityBinding
import com.aoztg.greengrim.presentation.ui.home.model.MoreActivity
import com.aoztg.greengrim.presentation.util.DefaultDiffUtil

class MoreActivityAdapter :
    ListAdapter<MoreActivity, MoreActivityViewHolder>(DefaultDiffUtil<MoreActivity>()) {

    override fun onBindViewHolder(holder: MoreActivityViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoreActivityViewHolder {
        return MoreActivityViewHolder(
            ItemHomeMoreActivityBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }
}

class MoreActivityViewHolder(private val binding: ItemHomeMoreActivityBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: MoreActivity) {
        binding.item = item
    }
}