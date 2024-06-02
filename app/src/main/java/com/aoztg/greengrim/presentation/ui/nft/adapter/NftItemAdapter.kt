package com.aoztg.greengrim.presentation.ui.nft.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aoztg.greengrim.databinding.ItemNftListBinding
import com.aoztg.greengrim.presentation.chatmanager.model.UiChatListItem
import com.aoztg.greengrim.presentation.ui.nft.model.UiNftItem
import com.aoztg.greengrim.presentation.util.DefaultDiffUtil

class NftItemAdapter : ListAdapter<UiNftItem, NftItemViewHolder>(diffCallback) {

    companion object {
        val diffCallback = object : DiffUtil.ItemCallback<UiNftItem>() {
            override fun areItemsTheSame(
                oldItem: UiNftItem,
                newItem: UiNftItem
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: UiNftItem,
                newItem: UiNftItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

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
        binding.btnHeart.setOnClickListener {
            item.clickLike(item.id)
        }
    }
}