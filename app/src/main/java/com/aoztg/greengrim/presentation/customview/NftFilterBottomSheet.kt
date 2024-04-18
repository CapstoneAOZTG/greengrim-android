package com.aoztg.greengrim.presentation.customview

import android.content.Context
import android.view.LayoutInflater
import androidx.core.content.ContextCompat
import com.aoztg.greengrim.R
import com.aoztg.greengrim.app.App
import com.aoztg.greengrim.databinding.BottomsheetNftFilterBinding
import com.google.android.material.bottomsheet.BottomSheetDialog

class NftFilterBottomSheet(
    context: Context,
    private val curPosition: NftSortType,
    private val onClickListener: (NftSortType) -> Unit
) : BottomSheetDialog(context) {

    private var binding: BottomsheetNftFilterBinding

    init {
        binding = BottomsheetNftFilterBinding.inflate(LayoutInflater.from(context))
        setContentView(binding.root)
        setBottomSheetListener()
    }

    private fun setBottomSheetListener() {
        when (curPosition) {
            NftSortType.DESC -> binding.btnDesc.setTextColor(
                ContextCompat.getColor(
                    App.context(),
                    R.color.white
                )
            )

            NftSortType.ASC -> binding.btnAsc.setTextColor(
                ContextCompat.getColor(
                    App.context(),
                    R.color.white
                )
            )

            NftSortType.FAVORITE -> binding.btnFavorite.setTextColor(
                ContextCompat.getColor(
                    App.context(),
                    R.color.white
                )
            )
        }
        binding.btnDesc.setOnClickListener {
            onClickListener(NftSortType.DESC)
            dismiss()
        }
        binding.btnAsc.setOnClickListener {
            onClickListener(NftSortType.ASC)
            dismiss()
        }
        binding.btnFavorite.setOnClickListener {
            onClickListener(NftSortType.FAVORITE)
            dismiss()
        }
    }
}

enum class NftSortType(val text: String, val value: String) {
    DESC("최신순", "DESC"),
    ASC("오래된 순", "ASC"),
    FAVORITE("좋아요 순", "FAVORITE")
}