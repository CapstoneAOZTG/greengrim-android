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

@BindingAdapter("balanceState", "checkState")
fun bindPurchaseBtnState(button: Button, balanceAfterPurchase: String, checkState: Boolean) {
    if (balanceAfterPurchase.isNotBlank()) {
        if (balanceAfterPurchase.toDouble() < 0.0) {
            button.text = "보유 KLAY 가 부족합니다"
            button.isEnabled = false
        } else {
            if(checkState){
                button.text = "구매하기"
                button.isEnabled =  true
            } else {
                button.text = "구매하기"
                button.isEnabled =  false
            }
        }
    }
}



