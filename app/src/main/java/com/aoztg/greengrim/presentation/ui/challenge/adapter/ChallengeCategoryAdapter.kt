package com.aoztg.greengrim.presentation.ui.challenge.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aoztg.greengrim.databinding.ItemChallengeCategoryBinding
import com.aoztg.greengrim.presentation.ui.challenge.model.ChallengeCategory


class ChallengeCategoryAdapter :
    ListAdapter<ChallengeCategory, ChallengeCategoryViewHolder>(ChallengeCategoryDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChallengeCategoryViewHolder {
        return ChallengeCategoryViewHolder(
            ItemChallengeCategoryBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ChallengeCategoryViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ChallengeCategoryDiffUtil : DiffUtil.ItemCallback<ChallengeCategory>() {
        override fun areContentsTheSame(
            oldItem: ChallengeCategory,
            newItem: ChallengeCategory
        ): Boolean {
            return oldItem.title == newItem.title
        }

        override fun areItemsTheSame(
            oldItem: ChallengeCategory,
            newItem: ChallengeCategory
        ): Boolean {
            return oldItem == newItem
        }
    }
}

class ChallengeCategoryViewHolder(private val binding: ItemChallengeCategoryBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: ChallengeCategory) {
        binding.item = item
    }

}