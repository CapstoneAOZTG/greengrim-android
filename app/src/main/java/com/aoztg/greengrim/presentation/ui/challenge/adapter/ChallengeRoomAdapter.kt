package com.aoztg.greengrim.presentation.ui.challenge.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aoztg.greengrim.databinding.ItemChallengeListBinding
import com.aoztg.greengrim.presentation.ui.challenge.model.ChallengeRoom

class ChallengeRoomAdapter :
    ListAdapter<ChallengeRoom, ChallengeRoomViewHolder>(ChallengeListDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChallengeRoomViewHolder {
        return ChallengeRoomViewHolder(
            ItemChallengeListBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ChallengeRoomViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ChallengeListDiffUtil : DiffUtil.ItemCallback<ChallengeRoom>() {
        override fun areContentsTheSame(oldItem: ChallengeRoom, newItem: ChallengeRoom): Boolean {
            return oldItem.title == newItem.title
        }

        override fun areItemsTheSame(oldItem: ChallengeRoom, newItem: ChallengeRoom): Boolean {
            return oldItem == newItem
        }
    }

}

class ChallengeRoomViewHolder(private val binding: ItemChallengeListBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: ChallengeRoom) {
        binding.item = item
    }
}