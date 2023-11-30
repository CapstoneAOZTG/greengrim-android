package com.aoztg.greengrim.presentation.bindingadapters

import android.widget.TextView
import androidx.databinding.BindingAdapter

@BindingAdapter("certificationRoundText")
fun bindCertificationRoundText(textView: TextView, round: Int) {
    textView.text = "${round}회차 인증"
}