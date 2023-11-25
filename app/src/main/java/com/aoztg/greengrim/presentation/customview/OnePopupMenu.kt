package com.aoztg.greengrim.presentation.customview

import android.content.Context
import android.content.res.Resources
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.PopupWindow
import com.aoztg.greengrim.databinding.DialogOnePopupMenuBinding
import com.aoztg.greengrim.presentation.util.Constants
import kotlin.math.roundToInt

class OnePopupMenu(
    private val context: Context,
    private inline val onClickAccusation: () -> Unit,
) {
    private val popUp by lazy {
        PopupWindow(binding.root, Constants.ONE_POPUP_WIDTH_DP.toPx(context.resources), Constants.ONE_POPUP_HEIGHT_DP.toPx(context.resources)).apply {
            elevation = 10f
        }
    }

    private val binding by lazy {
        DialogOnePopupMenuBinding.inflate(LayoutInflater.from(context)).apply {
            with(this) {
                root.setOnClickListener {
                    onClickAccusation()
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

    private fun Int.toPx(resource: Resources) = (resource.displayMetrics.density * this).roundToInt()
}