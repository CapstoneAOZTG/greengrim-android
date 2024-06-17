package com.aoztg.greengrim.presentation.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aoztg.greengrim.databinding.ItemHomeRecentIssuesBinding
import com.aoztg.greengrim.presentation.ui.home.model.UiHomeRecentIssue

class HomeRecentIssueAdapter(val data: List<UiHomeRecentIssue>) :
    RecyclerView.Adapter<HomeRecentIssueViewHolder>() {

    override fun onBindViewHolder(holder: HomeRecentIssueViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeRecentIssueViewHolder {
        return HomeRecentIssueViewHolder(
            ItemHomeRecentIssuesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount(): Int = data.size
}

class HomeRecentIssueViewHolder(private val binding: ItemHomeRecentIssuesBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: UiHomeRecentIssue) {
        binding.item = item
        binding.root.setOnClickListener {
            item.itemClickListener(item.id)
        }
    }
}