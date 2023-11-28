package com.aoztg.greengrim.presentation.ui.chat.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aoztg.greengrim.databinding.ItemChatBinding
import com.aoztg.greengrim.databinding.ItemChatDateBinding
import com.aoztg.greengrim.databinding.ItemMyChatBinding
import com.aoztg.greengrim.presentation.ui.chat.model.UiChatMessage
import com.aoztg.greengrim.presentation.util.Constants.DATE
import com.aoztg.greengrim.presentation.util.Constants.MY_CHAT
import com.aoztg.greengrim.presentation.util.Constants.OTHER_CHAT
import com.aoztg.greengrim.presentation.util.DefaultDiffUtil

class ChatMessageAdapter :
    ListAdapter<UiChatMessage, RecyclerView.ViewHolder>(DefaultDiffUtil<UiChatMessage>()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType){
            MY_CHAT -> {
                MyChatViewHolder(
                    ItemMyChatBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            OTHER_CHAT -> {
                OtherChatViewHolder(
                    ItemChatBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            DATE -> {
                ChatDateViewHolder(
                    ItemChatDateBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }

            else -> throw IllegalArgumentException("Invalid ViewType")
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(getItem(position).type){
            MY_CHAT -> (holder as MyChatViewHolder).bind(getItem(position))
            OTHER_CHAT -> (holder as OtherChatViewHolder).bind(getItem(position))
            DATE -> (holder as ChatDateViewHolder).bind(getItem(position))
        }
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position).type
    }
}

class OtherChatViewHolder(private val binding: ItemChatBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: UiChatMessage) {
        binding.item = item
        binding.ivImage.setOnClickListener {
            item.onCertClickListener(item.certId)
        }
    }

}

class MyChatViewHolder(private val binding: ItemMyChatBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: UiChatMessage){
        binding.item = item
        binding.ivImage.setOnClickListener {
            item.onCertClickListener(item.certId)
        }
    }
}

class ChatDateViewHolder(private val binding: ItemChatDateBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: UiChatMessage){
        binding.item = item
    }
}