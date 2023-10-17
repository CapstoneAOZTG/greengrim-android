package com.aoztg.greengrim.presentation.ui.home.adapter

import androidx.recyclerview.widget.RecyclerView
import com.aoztg.greengrim.R
import com.aoztg.greengrim.databinding.ItemHomeHotNftBinding
import com.aoztg.greengrim.presentation.ui.home.model.HomeUiModel

class HotNftHolder(private val binding : ItemHomeHotNftBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(
        item : HomeUiModel,
        listener : HomeItemClickListener?
    ){
        with(binding){
            tvTitle.text = item.title
            ivImage.setImageResource(R.drawable.test)
            ivProfile.setImageResource(R.drawable.test)
            tvUserName.text = item.userName
            tvCarbon.text = item.carbon
        }
    }
}