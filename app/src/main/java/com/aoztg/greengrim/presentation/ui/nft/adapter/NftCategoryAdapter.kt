package com.aoztg.greengrim.presentation.ui.nft.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aoztg.greengrim.databinding.ItemNftCategoryBinding
import com.aoztg.greengrim.presentation.ui.nft.model.UiNftCategory
import com.aoztg.greengrim.presentation.util.DefaultDiffUtil

class NftCategoryAdapter :
    ListAdapter<UiNftCategory, NftCategoryViewHolder>(DefaultDiffUtil<UiNftCategory>()) {

    override fun onBindViewHolder(holder: NftCategoryViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NftCategoryViewHolder {
        return NftCategoryViewHolder(
            ItemNftCategoryBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        )
    }

}

class NftCategoryViewHolder(private val binding: ItemNftCategoryBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: UiNftCategory) {
        binding.item = item
        binding.tvCount.text = "(${item.count})"
        binding.root.setOnClickListener {
            item.navigateToCollectionList(item.categoryName)
        }
    }
}