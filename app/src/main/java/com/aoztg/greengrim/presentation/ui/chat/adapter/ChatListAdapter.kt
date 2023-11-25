package com.aoztg.greengrim.presentation.ui.chat.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aoztg.greengrim.databinding.ItemChatListBinding
import com.aoztg.greengrim.presentation.ui.chat.model.ChatListItem
import com.aoztg.greengrim.presentation.util.DefaultDiffUtil

class ChatListAdapter : ListAdapter<ChatListItem, ChatListViewHolder>(
    DefaultDiffUtil<ChatListItem>()
) {

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

    fun bind(item: ChatListItem) {
        binding.item = item
        binding.root.setOnClickListener {
            item.onClickListener(item.chatId, item.challengeId)
        }
    }

}