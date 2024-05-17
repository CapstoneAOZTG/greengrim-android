package com.aoztg.greengrim.presentation.customview

import android.content.Context
import android.content.res.Resources
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.PopupWindow
import com.aoztg.greengrim.databinding.DialogChatPopupMenuBinding
import com.aoztg.greengrim.presentation.util.Constants
import kotlin.math.roundToInt

class ChatPopUpMenu(
    private val context: Context,
    private val onClickChallengeInfo: () -> Unit,
    private val onClickCertificationList: () -> Unit,
    private val onClickExit: () -> Unit
) {
    private val popUp by lazy {
        PopupWindow(
            binding.root,
            Constants.POPUP_WIDTH_DP.toPx(context.resources),
            Constants.THREE_POPUP_HEIGHT_DP.toPx(context.resources)
        ).apply {
            elevation = 10f
        }
    }

    private val binding by lazy {
        DialogChatPopupMenuBinding.inflate(LayoutInflater.from(context)).apply {
            with(this) {
                tvChallengeInfo.setOnClickListener {
                    dismiss()
                    onClickChallengeInfo()
                }
                tvCertificationList.setOnClickListener {
                    dismiss()
                    onClickCertificationList()
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