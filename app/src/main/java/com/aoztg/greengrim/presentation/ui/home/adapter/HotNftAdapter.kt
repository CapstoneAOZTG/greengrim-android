package com.aoztg.greengrim.presentation.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aoztg.greengrim.databinding.ItemHomeHotNftBinding
import com.aoztg.greengrim.databinding.ItemNftListBinding
import com.aoztg.greengrim.presentation.ui.market.model.UiNftItem

class HotNftAdapter(val data: List<UiNftItem>) : RecyclerView.Adapter<HotNftViewHolder>() {

    override fun onBindViewHolder(holder: HotNftViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HotNftViewHolder {
        return HotNftViewHolder(
            ItemHomeHotNftBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount(): Int = data.size
}

class HotNftViewHolder(private val binding: ItemHomeHotNftBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: UiNftItem) {
        binding.item = item
        binding.root.setOnClickListener {
            item.navigateToNftDetail(item.id)
        }
    }
}