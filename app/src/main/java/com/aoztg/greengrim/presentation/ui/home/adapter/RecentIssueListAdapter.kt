package com.aoztg.greengrim.presentation.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aoztg.greengrim.databinding.ItemIssueListBinding
import com.aoztg.greengrim.presentation.ui.home.model.UiRecentIssueItem
import com.aoztg.greengrim.presentation.util.DefaultDiffUtil

class RecentIssueListAdapter() : ListAdapter<UiRecentIssueItem, RecentIssueListViewHolder>(DefaultDiffUtil<UiRecentIssueItem>()) {

    override fun onBindViewHolder(holder: RecentIssueListViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecentIssueListViewHolder {
        return RecentIssueListViewHolder(
            ItemIssueListBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        )
    }
}

class RecentIssueListViewHolder(private val binding: ItemIssueListBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: UiRecentIssueItem) {
        binding.item = item
        binding.root.setOnClickListener {
            item.onItemClickListener(item.id)
        }
    }
}