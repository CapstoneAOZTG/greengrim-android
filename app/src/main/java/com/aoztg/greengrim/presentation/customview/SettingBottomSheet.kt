package com.aoztg.greengrim.presentation.customview

import android.content.Context
import android.view.LayoutInflater
import com.aoztg.greengrim.databinding.BottomsheetInfoSettingBinding
import com.google.android.material.bottomsheet.BottomSheetDialog

class SettingBottomSheet(
    context: Context,
    private val onEditProfileClickListener: () -> Unit,
    private val onNotificationClickListener: () -> Unit,
    private val onEditPasswordClickListener: () -> Unit,
    private val onAnnouncementClickListener: () -> Unit,
    private val onTermsClickListener: () -> Unit,
    private val onAppVersionClickListener: () -> Unit,
    private val onLogoutClickListener: () -> Unit,
    private val onWithdrawalClickListener: () -> Unit,
): BottomSheetDialog(context) {

    private var binding: BottomsheetInfoSettingBinding

    init{
        binding = BottomsheetInfoSettingBinding.inflate(LayoutInflater.from(context))
        setContentView(binding.root)
        setBottomSheetListener()
    }

    private fun setBottomSheetListener(){
        binding.tvEditProfile.setOnClickListener {
            onEditProfileClickListener()
            dismiss()
        }

        binding.tvNotification.setOnClickListener {
            onNotificationClickListener()
           dismiss()
        }

        binding.tvEditPassword.setOnClickListener {
            onEditPasswordClickListener()
            dismiss()
        }

        binding.tvAnnouncement.setOnClickListener {
            onAnnouncementClickListener()
            dismiss()
        }

        binding.tvTerms.setOnClickListener {
            onTermsClickListener()
            dismiss()
        }

        binding.tvAppVersion.setOnClickListener {
            onAppVersionClickListener()
            dismiss()
        }

        binding.tvLogout.setOnClickListener {
            onLogoutClickListener()
            dismiss()
        }

        binding.tvWithdrawal.setOnClickListener {
            onWithdrawalClickListener()
            dismiss()
        }
    }
}