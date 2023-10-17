package com.aoztg.greengrim.presentation.ui.home.adapter

import androidx.recyclerview.widget.RecyclerView
import com.aoztg.greengrim.R
import com.aoztg.greengrim.databinding.ItemHomeHotChallengeBinding
import com.aoztg.greengrim.presentation.App
import com.aoztg.greengrim.presentation.ui.home.model.HomeUiModel
import com.google.android.material.chip.Chip

class HotChallengeHolder(private val binding : ItemHomeHotChallengeBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(
        item : HomeUiModel,
        listener : HomeItemClickListener?
    ){
        with(binding){
            tvTitle.text = item.title
            ivImage.setImageResource(R.drawable.test)

//            item.chipList?.let{ chips ->
//                chips.forEach{
//                    val chip = Chip(App.context()).apply{
//                        text = it.text
//                        chipBackgroundColor = App.context().getColorStateList(R.color.gg_red)
//                        setTextColor(App.context().getColor(R.color.black))
//                    }
//
//                    this.itemChipgroup.addView(chip)
//                }
//            }
        }

    }
}