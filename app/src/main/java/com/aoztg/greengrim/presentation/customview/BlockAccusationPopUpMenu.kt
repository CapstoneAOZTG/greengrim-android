package com.aoztg.greengrim.presentation.customview

import android.content.Context
import android.content.res.Resources
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.PopupWindow
import com.aoztg.greengrim.databinding.DialogBlockAccusationPopupMenuBinding
import com.aoztg.greengrim.presentation.util.Constants
import kotlin.math.roundToInt

class BlockAccusationPopUpMenu(
    private val context: Context,
    private inline val onClickBlock: () -> Unit,
    private inline val onClickAccusation: () -> Unit,
) {
    private val popUp by lazy {
        PopupWindow(
            binding.root,
            Constants.POPUP_WIDTH_DP.toPx(context.resources),
            Constants.TWO_POPUP_HEIGHT_DP.toPx(context.resources)
        ).apply {
            elevation = 10f
        }
    }

    private val binding by lazy {
        DialogBlockAccusationPopupMenuBinding.inflate(LayoutInflater.from(context)).apply {
            with(this) {

                tvBlock.setOnClickListener {
                    onClickBlock()
                    dismiss()
                }

                tvAccusation.setOnClickListener {
                    onClickAccusation()
                    dismiss()
                }
            }
        }
    }

    fun show(xPosition: Int, yPosition: Int) {
        popUp.isOutsideTouchable = true
        popUp.showAtLocation(binding.root, Gravity.NO_GRAVITY, xPosition, yPosition)
    }

    fun dismiss() {
        popUp.dismiss()
    }

    private fun Int.toPx(resource: Resources) =
        (resource.displayMetrics.density * this).roundToInt()
}