package com.aoztg.greengrim.presentation.ui.chat.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aoztg.greengrim.databinding.ItemChatListBinding
import com.aoztg.greengrim.presentation.ui.chat.model.UiChatListItem

class ChatListAdapter : ListAdapter<UiChatListItem, ChatListViewHolder>(diffCallback) {

    companion object {
        val diffCallback = object : DiffUtil.ItemCallback<UiChatListItem>() {
            override fun areItemsTheSame(
                oldItem: UiChatListItem,
                newItem: UiChatListItem
            ): Boolean {
                return oldItem.chatId == newItem.chatId
            }

            override fun areContentsTheSame(
                oldItem: UiChatListItem,
                newItem: UiChatListItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatListViewHolder {
        return ChatListViewHolder(
            ItemChatListBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ChatListViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class ChatListViewHolder(private val binding: ItemChatListBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: UiChatListItem) {
        binding.item = item
        binding.root.setOnClickListener {
            item.onClickListener(item.title, item.chatId, item.challengeId)
        }
    }

}