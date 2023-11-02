package com.aoztg.greengrim.presentation.ui

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aoztg.greengrim.R
import com.bumptech.glide.Glide
import com.google.android.material.chip.ChipGroup

@BindingAdapter("imgResource")
fun bindResource(imageView: ImageView, resource: Int) {
    imageView.setImageResource(resource)
}

@BindingAdapter("imgUrl")
fun bindImg(imageView: ImageView, url: String) {

    Glide.with(imageView.context)
        .load(url)
        .error(R.drawable.test)
        .into(imageView)
}

@BindingAdapter("profileImgUrl")
fun bindProfileImg(imageView: ImageView, url: String) {
    if (url.isNotBlank()) {
        Glide.with(imageView.context)
            .load(url)
            .error(R.drawable.icon_profile)
            .into(imageView)
    }
}

@BindingAdapter("imgUrlCheckEmtpy")
fun bindImgCheckEmpty(imageView: ImageView, url: String) {
    if (url.isNotBlank()) {
        Glide.with(imageView.context)
            .load(url)
            .into(imageView)
    }
}

@BindingAdapter("colorChipList")
fun bindColorChips(chipGroup: ChipGroup, chips: List<String>) {

    chipGroup.removeAllViews()

    val colors = listOf(
        R.drawable.shape_greenfill_nostroke_radius20,
        R.drawable.shape_yellowfill_nostroke_radius20,
        R.drawable.shape_purplefill_nostroke_radius20,
        R.drawable.shape_grey2fill_nostroke_radius20
    )

    chips.forEach { data ->
        val chip = TextView(chipGroup.context).apply {
            text = data
            setBackgroundResource(colors.random())
            setTextAppearance(R.style.TextGgSmallBlackBold)
            setPadding(20, 4, 20, 4)
        }

        chipGroup.addView(chip)
    }
}

@BindingAdapter("greyChipList")
fun bindGreyChips(chipGroup: ChipGroup, chips: List<String>) {

    chipGroup.removeAllViews()

    chips.forEach { data ->
        val chip = TextView(chipGroup.context).apply {
            text = data
            setBackgroundResource(R.drawable.shape_grey2fill_nostroke_radius20)
            setTextAppearance(R.style.TextGgSmallBlackBold)
            setPadding(20, 4, 20, 4)
        }

        chipGroup.addView(chip)
    }
}

@BindingAdapter("list")
fun <T, VH : RecyclerView.ViewHolder> bindList(recyclerView: RecyclerView, list: List<T>) {
    val adapter = recyclerView.adapter as ListAdapter<T, VH>
    adapter.submitList(list)
}