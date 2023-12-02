package com.aoztg.greengrim.presentation.ui.challenge.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aoztg.greengrim.databinding.ItemChallengeCategoryBinding
import com.aoztg.greengrim.presentation.ui.challenge.model.UiChallengeCategory
import com.aoztg.greengrim.presentation.util.DefaultDiffUtil


class ChallengeCategoryAdapter(
    private val listener: OnCategoryItemClickListener
) :
    ListAdapter<UiChallengeCategory, ChallengeCategoryViewHolder>(DefaultDiffUtil<UiChallengeCategory>()) {

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
        holder.bind(getItem(position), listener)
    }
}

class ChallengeCategoryViewHolder(private val binding: ItemChallengeCategoryBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(
        item: UiChallengeCategory,
        listener: OnCategoryItemClickListener
    ) {
        binding.item = item
        binding.root.setOnClickListener {
            listener.onItemClicked(it,item.category)
        }
    }

}