package com.aoztg.greengrim.presentation.bindingadapters

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.aoztg.greengrim.R
import com.bumptech.glide.Glide

@BindingAdapter("chatCreationText")
fun bindChatListDday(textView: TextView, dDay: String) {
    if (dDay == "오늘") {
        textView.setBackgroundResource(R.drawable.shape_greenfill_nostroke_radius20)
    } else {
        textView.setBackgroundResource(R.drawable.shape_redfill_nostroke_radius20)
    }
    textView.text = dDay
}

@BindingAdapter("chatImgUrl")
fun bindChatImg(imageView: ImageView, url: String?) {
    if (url.isNullOrBlank()) {
        imageView.visibility = View.GONE
    } else {
        imageView.visibility = View.VISIBLE
        Glide.with(imageView.context)
            .load(url)
            .error(R.drawable.test)
            .into(imageView)
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