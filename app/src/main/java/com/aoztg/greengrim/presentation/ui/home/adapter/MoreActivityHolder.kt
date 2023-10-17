package com.aoztg.greengrim.presentation.ui.home.adapter

import androidx.recyclerview.widget.RecyclerView
import com.aoztg.greengrim.R
import com.aoztg.greengrim.databinding.ItemHomeMoreActivityBinding
import com.aoztg.greengrim.presentation.ui.home.model.HomeUiModel

class MoreActivityHolder(private val binding : ItemHomeMoreActivityBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(
        item : HomeUiModel,
        listener : HomeItemClickListener?
    ){
        with(binding){
            tvTitle.text = item.title
            ivImage.setImageResource(R.drawable.test)
            tvDetail.text = item.textDetail
            tvPoint.text = item.point

        }
    }
}