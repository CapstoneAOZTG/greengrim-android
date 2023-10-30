package com.aoztg.greengrim.presentation.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aoztg.greengrim.databinding.ItemHomeHotChallengeBinding
import com.aoztg.greengrim.presentation.ui.home.model.HotChallenge

class HotChallengeAdapter(val data: List<HotChallenge>) :
    RecyclerView.Adapter<HotChallengeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HotChallengeViewHolder {
        return HotChallengeViewHolder(
            ItemHomeHotChallengeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: HotChallengeViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int = data.size
}

class HotChallengeViewHolder(private val binding: ItemHomeHotChallengeBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: HotChallenge) {
        binding.item = item
    }
}