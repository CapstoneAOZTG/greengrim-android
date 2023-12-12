package com.aoztg.greengrim.presentation.customview

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import com.aoztg.greengrim.databinding.DialogCatchGameOverDialogBinding

class CatchGameOverDialog(
    context: Context,
    private val exitBtnClickListener: () -> Unit,
    private val restartBtnClickListener: () -> Unit
) : Dialog(context) {

    private lateinit var binding: DialogCatchGameOverDialogBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogCatchGameOverDialogBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
    }

    private fun initView() = with(binding) {
        btnExit.setOnClickListener {
            exitBtnClickListener()
            dismiss()
        }

        btnRestart.setOnClickListener {
            restartBtnClickListener()
            dismiss()
        }
    }
}