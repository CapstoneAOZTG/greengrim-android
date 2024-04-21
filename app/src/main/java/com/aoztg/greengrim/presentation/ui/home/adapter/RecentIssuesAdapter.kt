package com.aoztg.greengrim.presentation.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aoztg.greengrim.databinding.ItemHomeRecentIssuesBinding
import com.aoztg.greengrim.presentation.ui.home.model.UiMoreActivity

class RecentIssuesAdapter(val data: List<UiMoreActivity>) :
    RecyclerView.Adapter<RecentIssueViewHolder>() {

    override fun onBindViewHolder(holder: RecentIssueViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecentIssueViewHolder {
        return RecentIssueViewHolder(
            ItemHomeRecentIssuesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount(): Int = data.size
}

class RecentIssueViewHolder(private val binding: ItemHomeRecentIssuesBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: UiMoreActivity) {
        binding.item = item
        binding.ivIcon.setImageResource(item.imgResource)
        binding.root.setOnClickListener {
            item.itemClickListener()
        }
    }
}