package com.aoztg.greengrim.presentation.bindingadapters

import android.annotation.SuppressLint
import android.graphics.Color
import android.widget.Button
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.aoztg.greengrim.R
import com.aoztg.greengrim.presentation.ui.challenge.create.KeywordState
import com.aoztg.greengrim.presentation.ui.challenge.create.ProgressState
import com.aoztg.greengrim.presentation.ui.global.model.ChallengeDetail
import com.google.android.material.chip.ChipGroup

@SuppressLint("SetTextI18n")
@BindingAdapter("progressText")
fun bindProgressText(view: TextView, progressState: ProgressState) {
    when (progressState) {
        is ProgressState.Changed -> view.text = progressState.text
        else -> {}
    }
}

@BindingAdapter("selectChipList")
fun bindSelectChips(chipGroup: ChipGroup, keywordState: KeywordState) {
    when (keywordState) {
        is KeywordState.Set -> {
            chipGroup.removeAllViews()
            keywordState.keywords.forEach { data ->
                val chip = TextView(chipGroup.context).apply {
                    text = data
                    setBackgroundResource(R.drawable.shape_grey2fill_nostroke_radius20)
                    setTextAppearance(R.style.TextGgSmallBlackBold)
                    setPadding(20, 4, 20, 4)
                }

                chipGroup.addView(chip)
            }
        }

        else -> {}
    }
}

@BindingAdapter("challengeDetailBtnText")
fun bindChallengeDetailBtnText(button: Button, data: ChallengeDetail){

    if( data.entered ){
        button.text = "이미 참여중인 챌린지 입니다"
        button.isEnabled = false
        button.setTextColor(Color.WHITE)
    } else {
        button.text = "입장하기"
        button.isEnabled = true
        button.setTextColor(Color.BLACK)
    }
}