package com.aoztg.greengrim.presentation.customview

import android.content.Context
import android.view.LayoutInflater
import androidx.core.content.ContextCompat
import com.aoztg.greengrim.R
import com.aoztg.greengrim.app.App
import com.aoztg.greengrim.databinding.BottomsheetChallengeFilterBinding
import com.google.android.material.bottomsheet.BottomSheetDialog

class ChallengeFilterBottomSheet(
    context: Context,
    private val curPosition: ChallengeSortType,
    private val onClickListener: (ChallengeSortType) -> Unit
) : BottomSheetDialog(context) {

    private var binding: BottomsheetChallengeFilterBinding

    init {
        binding = BottomsheetChallengeFilterBinding.inflate(LayoutInflater.from(context))
        setContentView(binding.root)
        setBottomSheetListener()
    }

    private fun setBottomSheetListener() {
        when (curPosition) {
            ChallengeSortType.DESC -> binding.btnDesc.setTextColor(
                ContextCompat.getColor(
                    App.context(),
                    R.color.white
                )
            )

            ChallengeSortType.ASC -> binding.btnAsc.setTextColor(
                ContextCompat.getColor(
                    App.context(),
                    R.color.white
                )
            )

            ChallengeSortType.GREATEST -> binding.btnGreatest.setTextColor(
                ContextCompat.getColor(
                    App.context(),
                    R.color.white
                )
            )

            ChallengeSortType.LEAST -> binding.btnLeast.setTextColor(
                ContextCompat.getColor(
                    App.context(),
                    R.color.white
                )
            )
        }
        binding.btnDesc.setOnClickListener {
            onClickListener(ChallengeSortType.DESC)
            dismiss()
        }
        binding.btnAsc.setOnClickListener {
            onClickListener(ChallengeSortType.ASC)
            dismiss()
        }
        binding.btnGreatest.setOnClickListener {
            onClickListener(ChallengeSortType.GREATEST)
            dismiss()
        }
        binding.btnLeast.setOnClickListener {
            onClickListener(ChallengeSortType.LEAST)
            dismiss()
        }
    }

}

enum class ChallengeSortType(val text: String, val value: String) {
    DESC("최신순", "DESC"),
    ASC("오래된 순", "ASC"),
    GREATEST("인원 많은 순", "GREATEST"),
    LEAST("인원 적은 순", "LEAST")
}