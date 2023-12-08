package com.aoztg.greengrim.presentation.bindingadapters

import android.graphics.Color
import android.view.View
import android.widget.Button
import androidx.databinding.BindingAdapter
import com.aoztg.greengrim.presentation.ui.global.model.UiChallengeDetail

@BindingAdapter("challengeDetailBtnText")
fun bindChallengeDetailBtnText(button: Button, data: UiChallengeDetail) {

    if (data.entered) {
        button.text = "이미 참여중인 챌린지 입니다"
        button.isEnabled = false
        button.setTextColor(Color.WHITE)
    } else {
        button.text = "입장하기"
        button.isEnabled = true
        button.setTextColor(Color.BLACK)
    }
}

@BindingAdapter("checkBtnState")
fun bindCheckBtnState(button: Button, isVerified: String) {
    if (isVerified == "TRUE") {
        button.visibility = View.INVISIBLE
    } else {
        button.visibility = View.VISIBLE
    }
}

