package com.aoztg.greengrim.presentation.ui.home.adapter

import androidx.recyclerview.widget.RecyclerView
import com.aoztg.greengrim.R
import com.aoztg.greengrim.databinding.ItemHomeHotChallengeBinding
import com.aoztg.greengrim.presentation.ui.home.model.HomeUiModel

class HotChallengeHolder(private val binding: ItemHomeHotChallengeBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(
        item: HomeUiModel,
        listener: HomeItemClickListener?
    ) {
        with(binding) {
            tvTitle.text = item.title
            ivImage.setImageResource(R.drawable.test)

            val adapter = ChipAdapter()
            rvChipgroup.adapter = adapter
            adapter.submitList(item.chipList)
        }
    }
}