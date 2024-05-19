package com.aoztg.greengrim.presentation.customview

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import com.aoztg.greengrim.databinding.DialogAddWalletWarningBinding
import com.aoztg.greengrim.databinding.DialogExchangeNftBinding

class AddWalletWarningDialog(
    context: Context,
    private val walletAddress: String,
    private val confirmBtnClickListener: () -> Unit
) : Dialog(context) {

    private lateinit var binding: DialogAddWalletWarningBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogAddWalletWarningBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
    }

    private fun initView() = with(binding) {
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        tvWalletAddress.text = walletAddress
        btnCancel.setOnClickListener {
            dismiss()
        }
        btnConfirm.setOnClickListener {
            confirmBtnClickListener()
            dismiss()
        }
    }
}