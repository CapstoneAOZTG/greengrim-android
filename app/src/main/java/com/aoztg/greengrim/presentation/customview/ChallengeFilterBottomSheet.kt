package com.aoztg.greengrim.presentation.customview

import android.content.Context
import android.view.LayoutInflater
import androidx.core.content.ContextCompat
import com.aoztg.greengrim.R
import com.aoztg.greengrim.app.App
import com.aoztg.greengrim.databinding.BottomsheetChallengeFilterBinding
import com.aoztg.greengrim.presentation.ui.challenge.list.SortType
import com.google.android.material.bottomsheet.BottomSheetDialog

class ChallengeFilterBottomSheet(
    context: Context,
    private val curPosition: SortType,
    private val onClickListener: (SortType) -> Unit
): BottomSheetDialog(context) {

    private var binding: BottomsheetChallengeFilterBinding

    init{
        binding = BottomsheetChallengeFilterBinding.inflate(LayoutInflater.from(context))
        setContentView(binding.root)
        setBottomSheetListener()
    }

    private fun setBottomSheetListener(){
        when (curPosition) {
            SortType.DESC -> binding.btnDesc.setTextColor(
                ContextCompat.getColor(
                    App.context(),
                    R.color.white
                )
            )

            SortType.ASC -> binding.btnAsc.setTextColor(
                ContextCompat.getColor(
                    App.context(),
                    R.color.white
                )
            )

            SortType.GREATEST -> binding.btnGreatest.setTextColor(
                ContextCompat.getColor(
                    App.context(),
                    R.color.white
                )
            )

            SortType.LEAST -> binding.btnLeast.setTextColor(
                ContextCompat.getColor(
                    App.context(),
                    R.color.white
                )
            )
        }
        binding.btnDesc.setOnClickListener {
            onClickListener(SortType.DESC)
            dismiss()
        }
        binding.btnAsc.setOnClickListener {
            onClickListener(SortType.ASC)
            dismiss()
        }
        binding.btnGreatest.setOnClickListener {
            onClickListener(SortType.GREATEST)
            dismiss()
        }
        binding.btnLeast.setOnClickListener {
            onClickListener(SortType.LEAST)
            dismiss()
        }
    }

}