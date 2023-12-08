package com.aoztg.greengrim.presentation.customview

import android.content.Context
import android.view.LayoutInflater
import androidx.core.content.ContextCompat
import com.aoztg.greengrim.R
import com.aoztg.greengrim.app.App
import com.aoztg.greengrim.databinding.BottomsheetFilterBinding
import com.aoztg.greengrim.databinding.BottomsheetGrimNftFilterBinding
import com.aoztg.greengrim.databinding.BottomsheetInfoSettingBinding
import com.aoztg.greengrim.databinding.BottomsheetPhotoBinding
import com.aoztg.greengrim.presentation.ui.challenge.list.ChallengeSortType
import com.aoztg.greengrim.presentation.ui.market.GrimNftSortType
import com.google.android.material.bottomsheet.BottomSheetDialog

internal fun getSortSheet(
    context: Context,
    curPosition: ChallengeSortType,
    onClickListener: (ChallengeSortType) -> Unit
): BottomSheetDialog {
    val dialog = BottomSheetDialog(context)
    val binding = BottomsheetFilterBinding.inflate(LayoutInflater.from(context))
    dialog.setContentView(binding.root)

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
        dialog.dismiss()
    }
    binding.btnAsc.setOnClickListener {
        onClickListener(ChallengeSortType.ASC)
        dialog.dismiss()
    }
    binding.btnGreatest.setOnClickListener {
        onClickListener(ChallengeSortType.GREATEST)
        dialog.dismiss()
    }
    binding.btnLeast.setOnClickListener {
        onClickListener(ChallengeSortType.LEAST)
        dialog.dismiss()
    }

    return dialog
}

internal fun getGrimNftSortSheet(
    context: Context,
    curPosition: GrimNftSortType,
    onClickListener: (GrimNftSortType) -> Unit
): BottomSheetDialog {
    val dialog = BottomSheetDialog(context)
    val binding = BottomsheetGrimNftFilterBinding.inflate(LayoutInflater.from(context))
    dialog.setContentView(binding.root)

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
        dialog.dismiss()
    }
    binding.btnAsc.setOnClickListener {
        onClickListener(GrimNftSortType.ASC)
        dialog.dismiss()
    }
    return dialog
}

internal fun getInfoSettingSheet(
    context: Context,
    onEditProfileClickListener: () -> Unit,
    onNotificationClickListener: () -> Unit,
    onEditPasswordClickListener: () -> Unit,
    onAnnouncementClickListener: () -> Unit,
    onTermsClickListener: () -> Unit,
    onAppVersionClickListener: () -> Unit,
    onLogoutClickListener: () -> Unit,
    onWithdrawalClickListener: () -> Unit,
): BottomSheetDialog {
    val dialog = BottomSheetDialog(context)
    val binding = BottomsheetInfoSettingBinding.inflate(LayoutInflater.from(context))
    dialog.setContentView(binding.root)

    binding.tvEditProfile.setOnClickListener {
        onEditProfileClickListener()
        dialog.dismiss()
    }

    binding.tvNotification.setOnClickListener {
        onNotificationClickListener()
        dialog.dismiss()
    }

    binding.tvEditPassword.setOnClickListener {
        onEditPasswordClickListener()
        dialog.dismiss()
    }

    binding.tvAnnouncement.setOnClickListener {
        onAnnouncementClickListener()
        dialog.dismiss()
    }

    binding.tvTerms.setOnClickListener {
        onTermsClickListener()
        dialog.dismiss()
    }

    binding.tvAppVersion.setOnClickListener {
        onAppVersionClickListener()
        dialog.dismiss()
    }

    binding.tvLogout.setOnClickListener {
        onLogoutClickListener()
        dialog.dismiss()
    }

    binding.tvWithdrawal.setOnClickListener {
        onWithdrawalClickListener()
        dialog.dismiss()
    }

    return dialog
}

internal fun getPhotoSheet(
    context: Context,
    onPhotoClickListener: () -> Unit,
    onGalleryClickListener: () -> Unit,
) : BottomSheetDialog{
    val dialog = BottomSheetDialog(context)
    val binding = BottomsheetPhotoBinding.inflate(LayoutInflater.from(context))
    dialog.setContentView(binding.root)

    binding.tvCamera.setOnClickListener {
        onPhotoClickListener()
        dialog.dismiss()
    }

    binding.tvGallery.setOnClickListener {
        onGalleryClickListener()
        dialog.dismiss()
    }

    return dialog
}