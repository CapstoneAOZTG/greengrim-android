package com.aoztg.greengrim.presentation.customview

import android.content.Context
import android.view.LayoutInflater
import androidx.core.content.ContextCompat
import com.aoztg.greengrim.R
import com.aoztg.greengrim.app.App
import com.aoztg.greengrim.databinding.BottomsheetGrimNftFilterBinding
import com.aoztg.greengrim.presentation.ui.nft.GrimNftSortType
import com.google.android.material.bottomsheet.BottomSheetDialog

class GrimNftFilterBottomSheet(
    context: Context,
    private val curPosition: GrimNftSortType,
    private val onClickListener: (GrimNftSortType) -> Unit
): BottomSheetDialog(context) {

    private var binding: BottomsheetGrimNftFilterBinding

    init {
        binding = BottomsheetGrimNftFilterBinding.inflate(LayoutInflater.from(context))
        setContentView(binding.root)
        setBottomSheetListener()
    }

    private fun setBottomSheetListener(){
        when (curPosition) {
            GrimNftSortType.DESC -> binding.btnDesc.setTextColor(
                ContextCompat.getColor(
                    App.context(),
                    R.color.white
                )
            )

            GrimNftSortType.ASC -> binding.btnAsc.setTextColor(
                ContextCompat.getColor(
                    App.context(),
                    R.color.white
                )
            )
        }
        binding.btnDesc.setOnClickListener {
            onClickListener(GrimNftSortType.DESC)
            dismiss()
        }
        binding.btnAsc.setOnClickListener {
            onClickListener(GrimNftSortType.ASC)
            dismiss()
        }
    }
}