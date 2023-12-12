package com.aoztg.greengrim.presentation.customview

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import com.aoztg.greengrim.databinding.DialogYearMonthDatepickerBinding

class YearMonthPickerDialog(
    context: Context,
    private val curYear: Int,
    private val curMonth: Int,
    private val onConfirmBtnClickListener: (year: Int, month: Int) -> Unit
) : Dialog(context) {

    private lateinit var binding: DialogYearMonthDatepickerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DialogYearMonthDatepickerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding) {
            pickerMonth.minValue = 1
            pickerMonth.maxValue = 12
            pickerMonth.value = curMonth
            pickerYear.minValue = 1980
            pickerYear.maxValue = 2099
            pickerYear.value = curYear
        }

        setListener()
    }

    private fun setListener() {
        binding.btnCancel.setOnClickListener {
            this.dismiss()
        }

        binding.btnConfirm.setOnClickListener {
            onConfirmBtnClickListener(
                binding.pickerYear.value,
                binding.pickerMonth.value
            )
            dismiss()
        }
    }

    override fun show() {
        if (!this.isShowing) super.show()
    }

}