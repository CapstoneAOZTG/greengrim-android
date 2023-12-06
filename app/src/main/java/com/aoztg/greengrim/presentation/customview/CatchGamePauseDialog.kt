package com.aoztg.greengrim.presentation.customview

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import com.aoztg.greengrim.databinding.DialogCatchGamePauseDialogBinding

class CatchGamePauseDialog(
    context: Context,
    private val title: String,
    private val description: String,
    private val confirmBtnClickListener: () -> Unit
) : Dialog(context) {

    private lateinit var binding: DialogCatchGamePauseDialogBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogCatchGamePauseDialogBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
    }

    private fun initView() = with(binding) {
    }
}