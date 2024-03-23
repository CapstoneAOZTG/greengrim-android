package com.aoztg.greengrim.presentation.ui.nft.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aoztg.greengrim.databinding.ItemNftListBinding
import com.aoztg.greengrim.presentation.ui.nft.model.UiNftItem
import com.aoztg.greengrim.presentation.util.DefaultDiffUtil

class NftItemAdapter : ListAdapter<UiNftItem, NftItemViewHolder>(DefaultDiffUtil<UiNftItem>()) {

    override fun onBindViewHolder(holder: NftItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NftItemViewHolder {
        return NftItemViewHolder(
            ItemNftListBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        )
    }

}

class NftItemViewHolder(private val binding: ItemNftListBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: UiNftItem) {
        binding.item = item
        binding.root.setOnClickListener {
            item.navigateToNftDetail(item.id)
        }
    }
}