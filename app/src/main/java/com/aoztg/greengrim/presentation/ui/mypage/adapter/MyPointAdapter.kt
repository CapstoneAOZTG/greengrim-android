package com.aoztg.greengrim.presentation.ui.mypage.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aoztg.greengrim.databinding.ItemPointBinding
import com.aoztg.greengrim.presentation.ui.mypage.model.UiMyPointInfo
import com.aoztg.greengrim.presentation.util.DefaultDiffUtil

class MyPointAdapter : ListAdapter<UiMyPointInfo, MyPointViewHolder>(
    DefaultDiffUtil<UiMyPointInfo>()
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyPointViewHolder {
        return MyPointViewHolder(
            ItemPointBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: MyPointViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class MyPointViewHolder(private val binding: ItemPointBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: UiMyPointInfo) {
        binding.item = item
    }

}