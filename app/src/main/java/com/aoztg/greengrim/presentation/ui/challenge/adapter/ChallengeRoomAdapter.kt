package com.aoztg.greengrim.presentation.ui.challenge.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aoztg.greengrim.databinding.ItemChallengeListBinding
import com.aoztg.greengrim.presentation.ui.challenge.model.UiChallengeRoom
import com.aoztg.greengrim.presentation.util.DefaultDiffUtil

class ChallengeRoomAdapter :
    ListAdapter<UiChallengeRoom, ChallengeRoomViewHolder>(DefaultDiffUtil<UiChallengeRoom>()) {

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

}

class ChallengeRoomViewHolder(private val binding: ItemChallengeListBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: UiChallengeRoom) {
        binding.item = item
    }
}