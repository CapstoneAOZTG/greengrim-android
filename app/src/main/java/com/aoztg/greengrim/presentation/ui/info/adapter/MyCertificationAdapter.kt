package com.aoztg.greengrim.presentation.ui.info.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aoztg.greengrim.databinding.ItemMyCertificationBinding
import com.aoztg.greengrim.presentation.ui.info.model.UiMyCertification
import com.aoztg.greengrim.presentation.util.DefaultDiffUtil

class MyCertificationAdapter : ListAdapter<UiMyCertification, MyCertificationViewHolder>(DefaultDiffUtil<UiMyCertification>()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyCertificationViewHolder {
        return MyCertificationViewHolder(
            ItemMyCertificationBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: MyCertificationViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class MyCertificationViewHolder(private val binding: ItemMyCertificationBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: UiMyCertification) {
        binding.item = item
        binding.root.setOnClickListener {
            item.onItemClickListener(item.id)
        }
    }

}