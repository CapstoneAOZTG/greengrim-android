package com.aoztg.greengrim.presentation.ui.market.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aoztg.greengrim.databinding.ItemMyGrimForNftBinding
import com.aoztg.greengrim.presentation.ui.market.model.UiGrimForNft
import com.aoztg.greengrim.presentation.util.DefaultDiffUtil

class ChooseGrimAdapter: ListAdapter<UiGrimForNft, ChooseGrimViewHolder>(DefaultDiffUtil<UiGrimForNft>()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChooseGrimViewHolder {
        return ChooseGrimViewHolder(
            ItemMyGrimForNftBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ChooseGrimViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class ChooseGrimViewHolder(private val binding: ItemMyGrimForNftBinding): RecyclerView.ViewHolder(binding.root){

    fun bind(item: UiGrimForNft){
        binding.item = item
        binding.btnSelect.setOnClickListener {
            item.selectListener(item.id, item.image)
        }
    }
}