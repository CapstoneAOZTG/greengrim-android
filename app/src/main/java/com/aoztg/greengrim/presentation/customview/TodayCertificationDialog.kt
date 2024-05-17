package com.aoztg.greengrim.presentation.customview

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import com.aoztg.greengrim.databinding.DialogTodayCertificationBinding

class TodayCertificationDialog(
    context: Context,
    private val confirmBtnClickListener: () -> Unit
) : Dialog(context) {

    private lateinit var binding: DialogTodayCertificationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogTodayCertificationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
    }

    private fun initView() = with(binding) {
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        binding.btnCreateCertification.setOnClickListener {
            confirmBtnClickListener()
            dismiss()
        }

        binding.btnExit.setOnClickListener {
            dismiss()
        }
    }
}