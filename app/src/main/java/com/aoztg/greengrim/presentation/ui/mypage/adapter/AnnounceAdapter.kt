package com.aoztg.greengrim.presentation.ui.mypage.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aoztg.greengrim.databinding.ItemAnnounceBinding
import com.aoztg.greengrim.presentation.ui.mypage.model.UiAnnounceData
import com.aoztg.greengrim.presentation.util.DefaultDiffUtil

class AnnounceAdapter : ListAdapter<UiAnnounceData, AnnounceViewHolder>(
    DefaultDiffUtil<UiAnnounceData>()
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnnounceViewHolder {
        return AnnounceViewHolder(
            ItemAnnounceBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: AnnounceViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class AnnounceViewHolder(private val binding: ItemAnnounceBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: UiAnnounceData) {
        binding.item = item
        binding.root.setOnClickListener {
            item.onClickListener(item.id)
        }
    }

}