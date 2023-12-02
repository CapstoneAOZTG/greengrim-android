package com.aoztg.greengrim.presentation.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aoztg.greengrim.databinding.ItemHomeMoreActivityBinding
import com.aoztg.greengrim.presentation.ui.home.model.UiMoreActivity

class MoreActivityAdapter(val data: List<UiMoreActivity>) :
    RecyclerView.Adapter<MoreActivityViewHolder>() {

    override fun onBindViewHolder(holder: MoreActivityViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoreActivityViewHolder {
        return MoreActivityViewHolder(
            ItemHomeMoreActivityBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount(): Int = data.size
}

class MoreActivityViewHolder(private val binding: ItemHomeMoreActivityBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: UiMoreActivity) {
        binding.item = item
    }
}