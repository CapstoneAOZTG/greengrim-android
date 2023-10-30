package com.aoztg.greengrim.presentation.util

import android.content.Context
import android.view.LayoutInflater
import androidx.core.content.ContextCompat
import com.aoztg.greengrim.R
import com.aoztg.greengrim.app.App
import com.aoztg.greengrim.databinding.BottomsheetFilterBinding
import com.aoztg.greengrim.presentation.ui.challenge.list.ChallengeSortType
import com.google.android.material.bottomsheet.BottomSheetDialog

internal fun getSortSheet(context : Context, curPosition : ChallengeSortType, onClickListener : (ChallengeSortType) -> Unit) : BottomSheetDialog {
    val dialog = BottomSheetDialog(context)
    val binding = BottomsheetFilterBinding.inflate(LayoutInflater.from(context))
    dialog.setContentView(binding.root)

    when(curPosition){
        ChallengeSortType.RECENT-> binding.btnRecent.setTextColor(ContextCompat.getColor(App.context(), R.color.white))
        ChallengeSortType.MANY_PEOPLE-> binding.btnManyPeople.setTextColor(ContextCompat.getColor(App.context(), R.color.white))
        ChallengeSortType.LESS_PEOPLE-> binding.btnLessPeople.setTextColor(ContextCompat.getColor(App.context(), R.color.white))
    }
    binding.btnRecent.setOnClickListener {
        onClickListener(ChallengeSortType.RECENT)
        dialog.dismiss()
    }
    binding.btnManyPeople.setOnClickListener {
        onClickListener(ChallengeSortType.MANY_PEOPLE)
        dialog.dismiss()
    }
    binding.btnLessPeople.setOnClickListener {
        onClickListener(ChallengeSortType.LESS_PEOPLE)
        dialog.dismiss()
    }

    return dialog
}