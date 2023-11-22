package com.aoztg.greengrim.presentation.ui.chat.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aoztg.greengrim.databinding.ItemChatBinding
import com.aoztg.greengrim.databinding.ItemMyChatBinding
import com.aoztg.greengrim.presentation.ui.chat.model.ChatMessage
import com.aoztg.greengrim.presentation.util.Constants.MY_CHAT
import com.aoztg.greengrim.presentation.util.Constants.OTHER_CHAT
import com.aoztg.greengrim.presentation.util.DefaultDiffUtil

class ChatMessageAdapter :
    ListAdapter<ChatMessage, RecyclerView.ViewHolder>(DefaultDiffUtil<ChatMessage>()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == MY_CHAT) {
            return MyChatViewHolder(
                ItemMyChatBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        } else {
            return OtherChatViewHolder(
                ItemChatBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(getItem(position).type){
            MY_CHAT -> (holder as MyChatViewHolder).bind(getItem(position))
            OTHER_CHAT -> (holder as OtherChatViewHolder).bind(getItem(position))
        }
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position).type
    }
}

class OtherChatViewHolder(private val binding: ItemChatBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: ChatMessage) {
        binding.item = item
    }

}

class MyChatViewHolder(private val binding: ItemMyChatBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: ChatMessage){
        binding.item = item
    }
}