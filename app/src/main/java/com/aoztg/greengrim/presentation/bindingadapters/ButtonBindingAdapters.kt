package com.aoztg.greengrim.presentation.bindingadapters

import android.graphics.Color
import android.view.View
import android.widget.Button
import androidx.appcompat.widget.AppCompatButton
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

@BindingAdapter("nftDetailBtnState")
fun bindNftDetailBtnState(button: AppCompatButton, state: String){
    if(state.isNotBlank()){

        when(state){
            "CAN_SELL" -> {
                button.visibility = View.VISIBLE
                button.text = "판매하기"
            }
            "CAN_BUY" -> {
                button.visibility = View.VISIBLE
                button.text = "구매하기"
            }
        }
    }
}

