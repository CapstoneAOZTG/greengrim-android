package com.aoztg.greengrim.presentation.bindingadapters

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.aoztg.greengrim.R
import com.aoztg.greengrim.data.model.response.ChallengeDetailTags
import com.aoztg.greengrim.data.model.response.ChallengeSimpleTags
import com.aoztg.greengrim.data.model.response.HotChallengeTags
import com.aoztg.greengrim.presentation.ui.challenge.create.KeywordState
import com.aoztg.greengrim.presentation.ui.toCategoryText
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup


@BindingAdapter("keywordChips")
fun bindKeywordChips(chipGroup: ChipGroup, keyword: List<String>){
    chipGroup.removeAllViews()
    keyword.forEach {
        val chip = TextView(chipGroup.context).apply{
            setBackgroundResource(R.drawable.shape_grey7fill_nostroke_15radius)
            text = it
            setTextAppearance(R.style.TextGgSmallBold)
            setPadding(30, 20, 30, 20)
        }
        chipGroup.addView(chip)
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

    if (chips != null) {
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

@BindingAdapter("hotChipList")
fun bindHotChips(chipGroup: ChipGroup, chips: HotChallengeTags) {

    if (chips != null) {
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