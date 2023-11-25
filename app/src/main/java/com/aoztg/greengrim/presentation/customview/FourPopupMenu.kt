package com.aoztg.greengrim.presentation.customview

import android.content.Context
import android.content.res.Resources
import android.graphics.drawable.BitmapDrawable
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.PopupWindow
import com.aoztg.greengrim.databinding.DialogFourPopupMenuBinding
import com.aoztg.greengrim.presentation.util.Constants
import kotlin.math.roundToInt

class FourPopupMenu(
    private val context: Context,
    private val onClickChallengeInfo: () -> Unit,
    private val onClickCertificationList: () -> Unit,
    private val onClickAccusation: () -> Unit,
    private val onClickExit: () -> Unit
) {
    private val popUp by lazy {
        PopupWindow(
            binding.root,
            Constants.ONE_POPUP_WIDTH_DP.toPx(context.resources),
            Constants.FOUR_POPUP_HEIGHT_DP.toPx(context.resources)
        ).apply {
            elevation = 10f
        }
    }

    private val binding by lazy {
        DialogFourPopupMenuBinding.inflate(LayoutInflater.from(context)).apply {
            with(this) {
                tvChallengeInfo.setOnClickListener {
                    dismiss()
                    onClickChallengeInfo()
                }
                tvCertificationList.setOnClickListener {
                    dismiss()
                    onClickCertificationList()
                }
                tvAccusation.setOnClickListener {
                    dismiss()
                    onClickAccusation()
                }
                tvExit.setOnClickListener {
                    dismiss()
                    onClickExit()
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