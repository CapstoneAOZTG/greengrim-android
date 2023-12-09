package com.aoztg.greengrim.presentation.customview

import android.content.Context
import android.view.LayoutInflater
import com.aoztg.greengrim.databinding.BottomsheetPhotoBinding
import com.google.android.material.bottomsheet.BottomSheetDialog

class PhotoBottomSheet(
    context: Context,
    private val onPhotoClickListener: () -> Unit,
    private val onGalleryClickListener: () -> Unit,
):BottomSheetDialog(context) {

    private var binding: BottomsheetPhotoBinding

    init{
        binding = BottomsheetPhotoBinding.inflate(LayoutInflater.from(context))
        setContentView(binding.root)
        setBottomSheetListener()
    }

    private fun setBottomSheetListener(){
        binding.tvCamera.setOnClickListener {
            onPhotoClickListener()
            dismiss()
        }

        binding.tvGallery.setOnClickListener {
            onGalleryClickListener()
            dismiss()
        }
    }
}