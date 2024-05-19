package com.aoztg.greengrim.presentation.bindingadapters

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.aoztg.greengrim.R
import com.aoztg.greengrim.data.model.response.ChallengeDetailTags
import com.aoztg.greengrim.data.model.response.ChallengeSimpleTags
import com.aoztg.greengrim.presentation.ui.toCategoryText
import com.google.android.material.chip.ChipGroup


@BindingAdapter("challengeListChips")
fun bindChallengeListChips(chipGroup: ChipGroup, chips: ChallengeSimpleTags) {

    chipGroup.removeAllViews()

    val chipList = mutableListOf<TextView>()

    chipList.add(TextView(chipGroup.context).apply {
        text = chips.category.toCategoryText()
    })

    chipList.add(TextView(chipGroup.context).apply {
        text = chips.participantCount
    })

    chipList.add(TextView(chipGroup.context).apply {
        text = chips.goalCount
    })

    chipList.forEach { chip ->
        chip.apply {
            setBackgroundResource(R.drawable.shape_nofill_whitestroke_radius15)
            setTextAppearance(R.style.TextGgSmallBold)
            setPadding(40, 16, 40, 20)
        }
        chipGroup.addView(chip)
    }
}

@BindingAdapter("mainChipList")
fun bindDetailMainChips(chipGroup: ChipGroup, chips: ChallengeDetailTags?) {

    if (chips != null) {
        chipGroup.removeAllViews()

        val chipList = mutableListOf<TextView>()

        chipList.add(TextView(chipGroup.context).apply {
            text = chips.category.toCategoryText()
        })

        chipList.add(TextView(chipGroup.context).apply {
            text = chips.participantCount
        })

        chipList.add(TextView(chipGroup.context).apply {
            text = chips.goalCount
        })

        chipList.forEach { chip ->
            chip.apply {
                setBackgroundResource(R.drawable.shape_nofill_whitestroke_radius15)
                setTextAppearance(R.style.TextGgSmallBold)
                setPadding(40, 16, 40, 20)
            }
            chipGroup.addView(chip)
        }
    }
}

