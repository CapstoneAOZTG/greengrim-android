package com.aoztg.greengrim.presentation.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aoztg.greengrim.databinding.ItemHomeHotChallengeBinding
import com.aoztg.greengrim.presentation.ui.home.model.HotChallenge
import com.aoztg.greengrim.presentation.util.DefaultDiffUtil

class HotChallengeAdapter :
    ListAdapter<HotChallenge, HotChallengeViewHolder>(DefaultDiffUtil<HotChallenge>()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HotChallengeViewHolder {
        return HotChallengeViewHolder(
            ItemHomeHotChallengeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: HotChallengeViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class HotChallengeViewHolder(private val binding: ItemHomeHotChallengeBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: HotChallenge) {
        binding.item = item
    }
}