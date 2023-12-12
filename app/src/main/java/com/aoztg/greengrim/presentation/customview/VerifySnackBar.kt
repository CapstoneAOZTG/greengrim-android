package com.aoztg.greengrim.presentation.customview

import android.view.LayoutInflater
import android.view.View
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.aoztg.greengrim.R
import com.aoztg.greengrim.databinding.DialogVerifySnackbarBinding
import com.google.android.material.snackbar.Snackbar

class VerifySnackBar(
    view: View
) {

    companion object{

        fun make(view: View) = VerifySnackBar(view)
    }

    private val context = view.context
    private val snackbar = Snackbar.make(view, "", 5000).apply {
        anchorView = view
    }
    private val snackbarLayout = snackbar.view as Snackbar.SnackbarLayout

    private val binding: DialogVerifySnackbarBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.dialog_verify_snackbar, null, false)

    init {
        initView()
    }

    private fun initView() {
        with(snackbarLayout) {
            removeAllViews()
            setPadding(40, 0, 40, 0)
            setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent))
            addView(binding.root, 0)
        }
    }

    fun show() {
        snackbar.show()
    }
}