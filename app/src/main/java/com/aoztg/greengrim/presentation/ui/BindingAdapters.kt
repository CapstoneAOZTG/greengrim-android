package com.aoztg.greengrim.presentation.ui

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aoztg.greengrim.R
import com.aoztg.greengrim.data.model.ChallengeDetailTags
import com.aoztg.greengrim.data.model.ChallengeSimpleTags
import com.aoztg.greengrim.presentation.util.toCategoryText
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

@BindingAdapter("challengeListChips")
fun bindChallengeListChips(chipGroup: ChipGroup, chips: ChallengeSimpleTags) {

    chipGroup.removeAllViews()

    val chipList = mutableListOf<TextView>()

    chipList.add(TextView(chipGroup.context).apply {
        text = chips.category.toCategoryText()
        setBackgroundResource(R.drawable.shape_purplefill_nostroke_radius20)
    })

    chipList.add(TextView(chipGroup.context).apply {
        text = chips.ticketCount
        setBackgroundResource(R.drawable.shape_yellowfill_nostroke_radius20)
    })

    chipList.add(TextView(chipGroup.context).apply {
        text = chips.goalCount
        setBackgroundResource(R.drawable.shape_grey2fill_nostroke_radius20)
    })

    chipList.add(TextView(chipGroup.context).apply {
        text = chips.keyword
        setBackgroundResource(R.drawable.shape_grey2fill_nostroke_radius20)
    })

    chipList.forEach { chip ->
        chip.apply {
            setTextAppearance(R.style.TextGgSmallBlackBold)
            setPadding(20, 4, 20, 4)
        }
        chipGroup.addView(chip)
    }

}

@BindingAdapter("mainChipList")
fun bindDetailMainChips(chipGroup: ChipGroup, chips: ChallengeDetailTags?) {


    if(chips != null){
        chipGroup.removeAllViews()

        val chipList = mutableListOf<TextView>()

        chipList.add(TextView(chipGroup.context).apply {
            text = chips.category.toCategoryText()
            setBackgroundResource(R.drawable.shape_purplefill_nostroke_radius20)
        })

        chipList.add(TextView(chipGroup.context).apply {
            text = chips.ticketCount
            setBackgroundResource(R.drawable.shape_yellowfill_nostroke_radius20)
        })

        chipList.forEach { chip ->
            chip.apply {
                setTextAppearance(R.style.TextGgSmallBlackBold)
                setPadding(20, 4, 20, 4)
            }
            chipGroup.addView(chip)
        }
    }

}

@BindingAdapter("subChipList")
fun bindDetailSubChips(chipGroup: ChipGroup, chips: ChallengeDetailTags?) {

    if(chips != null){
        chipGroup.removeAllViews()

        val chipList = mutableListOf<TextView>()

        chipList.add(TextView(chipGroup.context).apply {
            text = chips.goalCount
        })
        chipList.add(TextView(chipGroup.context).apply {
            text = chips.weekMinCount
        })
        chipList.add(TextView(chipGroup.context).apply {
            text = chips.keyword
        })
        chipList.add(TextView(chipGroup.context).apply {
            text = chips.participantCount
        })

        chipList.forEach { chip ->
            chip.apply {
                setBackgroundResource(R.drawable.shape_grey2fill_nostroke_radius20)
                setTextAppearance(R.style.TextGgSmallBlackBold)
                setPadding(20, 4, 20, 4)
            }
            chipGroup.addView(chip)
        }
    }
}

@BindingAdapter("list")
fun <T, VH : RecyclerView.ViewHolder> bindList(recyclerView: RecyclerView, list: List<T>) {
    val adapter = recyclerView.adapter as ListAdapter<T, VH>
    adapter.submitList(list)
}