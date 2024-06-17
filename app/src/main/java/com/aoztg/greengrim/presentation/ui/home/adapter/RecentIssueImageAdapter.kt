package com.aoztg.greengrim.presentation.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aoztg.greengrim.databinding.ItemIssueImgBinding
import com.aoztg.greengrim.databinding.ItemIssueListBinding
import com.aoztg.greengrim.presentation.ui.home.model.UiRecentIssueItem
import com.aoztg.greengrim.presentation.util.DefaultDiffUtil
import com.bumptech.glide.Glide

class RecentIssueImageAdapter() : ListAdapter<String, RecentIssueImageViewHolder>(
    DefaultDiffUtil<String>()
) {

    override fun onBindViewHolder(holder: RecentIssueImageViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecentIssueImageViewHolder {
        return RecentIssueImageViewHolder(
            ItemIssueImgBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        )
    }
}

class RecentIssueImageViewHolder(private val binding: ItemIssueImgBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: String) {
        Glide.with(binding.ivImage.context)
            .load(item)
            .into(binding.ivImage)
    }
}