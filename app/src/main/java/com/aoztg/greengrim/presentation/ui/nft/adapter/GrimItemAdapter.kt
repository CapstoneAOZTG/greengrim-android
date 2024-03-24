package com.aoztg.greengrim.presentation.ui.nft.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aoztg.greengrim.databinding.ItemPaintListBinding
import com.aoztg.greengrim.presentation.ui.nft.model.UiGrimItem
import com.aoztg.greengrim.presentation.util.DefaultDiffUtil

class GrimItemAdapter: ListAdapter<UiGrimItem, GrimItemViewHolder>(DefaultDiffUtil<UiGrimItem>()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GrimItemViewHolder {
        return GrimItemViewHolder(
            ItemPaintListBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: GrimItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class GrimItemViewHolder(private val binding: ItemPaintListBinding): RecyclerView.ViewHolder(binding.root){

    fun bind(item: UiGrimItem){
        binding.item = item
        binding.root.setOnClickListener {
            item.navigateToGrimDetail(item.id)
        }
    }
}