package com.aoztg.greengrim.presentation.bindingadapters

import android.annotation.SuppressLint
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.aoztg.greengrim.R
import com.aoztg.greengrim.presentation.ui.BaseUiState
import com.aoztg.greengrim.presentation.ui.challenge.create.ProgressState

@BindingAdapter("checkCompleteViewState")
fun bindCheckCompleteViewState(textView: TextView, isVerified: String) {
    if (isVerified == "TRUE") {
        textView.visibility = View.VISIBLE
    } else {
        textView.visibility = View.INVISIBLE
    }
}

@SuppressLint("SetTextI18n")
@BindingAdapter("textLength", "textLimit")
fun bindTextLength(view: TextView, text: String, limit: Int) {
    view.text = "(${text.length}/$limit)"
}

@BindingAdapter("checkAnnounceViewState")
fun bindCheckAnnounceViewState(textView: TextView, isVerified: String) {
    if (isVerified == "DEACTIVATION") {
        textView.visibility = View.INVISIBLE
    } else {
        textView.visibility = View.VISIBLE
    }
}

@BindingAdapter("unReadChatCount")
fun bindUnReadChatCount(tv: TextView, count: Int) {
    if (count == 0) {
        tv.visibility = View.INVISIBLE
    } else {
        tv.visibility = View.VISIBLE
        tv.text = count.toString()
    }
}

@BindingAdapter("descriptionHelperMessage")
fun bindDescriptionHelperMessage(tv: TextView, state: BaseUiState) {
    when (state) {
        is BaseUiState.Error -> {
            tv.visibility = View.VISIBLE
        }

        else -> tv.visibility = View.GONE
    }
}

@SuppressLint("SetTextI18n")
@BindingAdapter("progressText")
fun bindProgressText(view: TextView, progressState: ProgressState) {
    when (progressState) {
        is ProgressState.Changed -> view.text = progressState.text
        else -> {}
    }
}

@BindingAdapter("certificationRoundText")
fun bindCertificationRoundText(textView: TextView, round: Int) {
    textView.text = "${round}회차 인증"
}

@BindingAdapter("greenHighLightText", "greenHighLightsIndex", "greenHighLighteIndex")
fun bindGreenHighLightText(tv: TextView, text: String, sIndex: Int, eIndex: Int) {
    val spannable =
        SpannableString(text)
            .apply {
                setSpan(
                    ForegroundColorSpan(tv.context.getColor(R.color.gg_green)),
                    sIndex,
                    eIndex,
                    Spannable.SPAN_EXCLUSIVE_INCLUSIVE
                )
            }
    tv.text = spannable
}