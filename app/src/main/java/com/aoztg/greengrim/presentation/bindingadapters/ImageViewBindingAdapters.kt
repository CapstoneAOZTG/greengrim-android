package com.aoztg.greengrim.presentation.bindingadapters

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.aoztg.greengrim.R
import com.bumptech.glide.Glide

@BindingAdapter("imgResource")
fun bindResource(imageView: ImageView, resource: Int) {
    imageView.setImageResource(resource)
}

@BindingAdapter("imgUrl")
fun bindImg(imageView: ImageView, url: String) {
    Glide.with(imageView.context)
        .load(url)
        .error(R.drawable.icon_no_image)
        .into(imageView)
}

@BindingAdapter("profileImgUrl")
fun bindProfileImg(imageView: ImageView, url: String) {
    Glide.with(imageView.context)
        .load(url)
        .error(R.drawable.icon_profile)
        .into(imageView)
}

@BindingAdapter("imgUrlCheckEmtpy")
fun bindImgCheckEmpty(imageView: ImageView, url: String) {
    if (url.isNotBlank()) {
        Glide.with(imageView.context)
            .load(url)
            .into(imageView)
    }
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

@BindingAdapter("noListState")
fun <T> bindNoList(view: ImageView, list: List<T>) {
    if (list.isEmpty()) {
        view.visibility = View.VISIBLE
    } else {
        view.visibility = View.INVISIBLE
    }
}