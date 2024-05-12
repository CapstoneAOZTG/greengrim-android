package com.aoztg.greengrim.presentation.customview

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import androidx.core.widget.doOnTextChanged
import com.aoztg.greengrim.R
import com.aoztg.greengrim.databinding.DialogAccusationBinding

class AccusationDialog(
    context: Context,
    private val title: String,
    private val accusationClickListener: (AccusationContentType, String) -> Unit
) : Dialog(context) {
    private lateinit var binding: DialogAccusationBinding
    private var reason = AccusationContentType.PROMOTIONAL_POST
    private var content = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogAccusationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
    }

    private fun initView() = with(binding) {
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        tvAccusation.text = title

        btnAccusation.setOnClickListener {
            accusationClickListener(reason, content)
        }

        btnCancel.setOnClickListener {
            dismiss()
        }

        rgType.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.btn_inappropriate_publicity -> {
                    etReason.visibility = View.GONE
                    reason = AccusationContentType.PROMOTIONAL_POST
                }

                R.id.btn_content_containing_obscenity -> {
                    etReason.visibility = View.GONE
                    reason = AccusationContentType.LEWDNESS
                }

                R.id.btn_bad_words -> {
                    etReason.visibility = View.GONE
                    reason = AccusationContentType.HATRED
                }

                R.id.btn_infringement_rights -> {
                    etReason.visibility = View.GONE
                    reason = AccusationContentType.INFRINGEMENT
                }

                R.id.btn_etc -> {
                    etReason.visibility = View.VISIBLE
                    reason = AccusationContentType.ETC
                }
            }
        }

        etReason.doOnTextChanged { text, _, _, _ ->
            content = text.toString()
        }
    }

}

enum class AccusationContentType(val text: String) {
    PROMOTIONAL_POST("PROMOTIONAL_POST"),
    LEWDNESS("LEWDNESS"),
    HATRED("HATRED"),
    INFRINGEMENT("INFRINGEMENT"),
    ETC("ETC")
}

enum class AccusationType(val text: String) {
    MEMBER("MEMBER"),
    CHALLENGE("CHALLENGE"),
    CERTIFICATION("CERTIFICATION"),
    NFT("NFT")
}