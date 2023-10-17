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

            item.chipList?.let{ chips ->
                chips.forEachIndexed{ index, data ->
                    val chip = Chip(binding.itemChipgroup.context).apply{
                        text = data
                        when(index){
                            0 -> { chipBackgroundColor = App.context().getColorStateList(R.color.gg_purple) }
                            1 -> { chipBackgroundColor = App.context().getColorStateList(R.color.gg_yellow) }
                            2 -> { chipBackgroundColor = App.context().getColorStateList(R.color.gg_grey2) }
                        }
                        setEnsureMinTouchTargetSize(false)
                        chipCornerRadius = 40F
                        chipMinHeight = 30F
                        setTextAppearance(R.style.TextGgSmallBlack)
                        setChipStrokeColorResource(android.R.color.transparent)
                        chipStartPadding = 0F
                        chipEndPadding = 0F
                    }

                    this.itemChipgroup.addView(chip)
                }
            }
        }
    }


}