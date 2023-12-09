package com.aoztg.greengrim.presentation.customview

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.WindowManager
import com.aoztg.greengrim.databinding.DialogTwoButtonTitleBinding

class TwoButtonTitleDialog(
    context: Context,
    private val title: String,
    private val description: String,
    private val confirmBtnClickListener: () -> Unit
) : Dialog(context) {

    private lateinit var binding: DialogTwoButtonTitleBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogTwoButtonTitleBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
    }

    private fun initView() = with(binding) {
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        tvTitle.text = title
        tvDescription.text = description
        btnCancel.setOnClickListener {
            dismiss()
        }
        btnConfirm.setOnClickListener {
            confirmBtnClickListener()
            dismiss()
        }
    }
}