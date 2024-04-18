package com.aoztg.greengrim.presentation.customview

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import com.aoztg.greengrim.databinding.DialogExchangeNftBinding

class ExchangeNftDialog(
    context: Context,
    private val point: Int,
    private val confirmBtnClickListener: () -> Unit
) : Dialog(context) {

    private lateinit var binding: DialogExchangeNftBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogExchangeNftBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
    }

    private fun initView() = with(binding) {
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        tvTitle.text = "${point} Green point가 소진됩니다!\n이 NFT로 교환할까요?"
        btnCancel.setOnClickListener {
            dismiss()
        }
        btnConfirm.setOnClickListener {
            confirmBtnClickListener()
            dismiss()
        }
    }
}