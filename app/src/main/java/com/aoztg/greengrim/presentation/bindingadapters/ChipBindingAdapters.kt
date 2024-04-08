package com.aoztg.greengrim.presentation.bindingadapters

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.aoztg.greengrim.R
import com.aoztg.greengrim.data.model.response.ChallengeDetailTags
import com.aoztg.greengrim.data.model.response.ChallengeSimpleTags
import com.aoztg.greengrim.presentation.ui.challenge.create.KeywordState
import com.aoztg.greengrim.presentation.ui.toCategoryText
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup


@BindingAdapter("challengeListChips")
fun bindChallengeListChips(chipGroup: ChipGroup, chips: ChallengeSimpleTags) {

    chipGroup.removeAllViews()

    val chipList = mutableListOf<TextView>()

    chipList.add(TextView(chipGroup.context).apply {
        text = chips.category.toCategoryText()
    })

    chipList.add(TextView(chipGroup.context).apply {
        text = chips.ticketCount
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
            text = chips.ticketCount
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

@BindingAdapter("subChipList")
fun bindDetailSubChips(chipGroup: ChipGroup, chips: ChallengeDetailTags?) {

    if (chips != null) {
        chipGroup.removeAllViews()

        val chipList = mutableListOf<TextView>()

        chipList.add(TextView(chipGroup.context).apply {
            text = chips.goalCount
        })
        chipList.add(TextView(chipGroup.context).apply {
            text = chips.weekMinCount
        })
        chipList.add(TextView(chipGroup.context).apply {
            text = chips.participantCount
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


@BindingAdapter("selectChipList")
fun bindSelectChips(chipGroup: ChipGroup, keywordState: KeywordState) {
    when (keywordState) {
        is KeywordState.Set -> {
            chipGroup.removeAllViews()
            keywordState.keywords.forEach { data ->
                val chip = TextView(chipGroup.context).apply {
                    text = data
                    setBackgroundResource(R.drawable.shape_grey2fill_nostroke_radius20)
                    setTextAppearance(R.style.TextGgSmallBlackBold)
                    setPadding(20, 4, 20, 4)
                }

                chipGroup.addView(chip)
            }
        }

        else -> {}
    }
}