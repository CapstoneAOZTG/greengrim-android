package com.aoztg.greengrim.presentation.ui.nft.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aoztg.greengrim.databinding.ItemNftCollectionListBinding
import com.aoztg.greengrim.presentation.ui.nft.model.UiNftCollectionItem
import com.aoztg.greengrim.presentation.util.DefaultDiffUtil

class NftCollectionAdapter :
    ListAdapter<UiNftCollectionItem, NftCollectionViewHolder>(DefaultDiffUtil<UiNftCollectionItem>()) {

    override fun onBindViewHolder(holder: NftCollectionViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NftCollectionViewHolder {
        return NftCollectionViewHolder(
            ItemNftCollectionListBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        )
    }

}

class NftCollectionViewHolder(private val binding: ItemNftCollectionListBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: UiNftCollectionItem) {
        binding.item = item
        binding.root.setOnClickListener {
            item.navigateToDetail(item.id)
        }
    }
}