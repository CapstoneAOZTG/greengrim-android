package com.aoztg.greengrim.presentation.customview

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import com.aoztg.greengrim.databinding.DialogPermissionBinding

class PermissionDialog(
    context: Context,
    private val confirmBtnClickListener: () -> Unit
) : Dialog(context) {

    private lateinit var binding: DialogPermissionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogPermissionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
    }

    private fun initView() = with(binding) {
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        binding.btnCheck.setOnClickListener {
            confirmBtnClickListener()
        }
    }
}