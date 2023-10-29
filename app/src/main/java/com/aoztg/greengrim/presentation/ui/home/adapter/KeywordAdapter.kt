package com.aoztg.greengrim.presentation.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aoztg.greengrim.R
import com.aoztg.greengrim.databinding.ItemHotChallengeChipsBinding
import com.aoztg.greengrim.presentation.util.DefaultDiffUtil

class ChipAdapter : ListAdapter<String, KeywordViewHolder>(DefaultDiffUtil<String>()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KeywordViewHolder {
        return KeywordViewHolder(
            ItemHotChallengeChipsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: KeywordViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class KeywordViewHolder(private val binding: ItemHotChallengeChipsBinding) :
    RecyclerView.ViewHolder(binding.root) {

    private val colors = listOf(
        R.drawable.shape_greenfill_nostroke_radius20,
        R.drawable.shape_yellowfill_nostroke_radius20,
        R.drawable.shape_purplefill_nostroke_radius20,
        R.drawable.shape_grey2fill_nostroke_radius20
    )

    fun bind(keyword: String) {
        binding.tvKeyword.text = keyword
        binding.tvKeyword.setBackgroundResource(colors.random())
    }

}