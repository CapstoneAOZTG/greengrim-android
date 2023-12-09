package com.aoztg.greengrim.presentation.ui.global.paintdetail

import androidx.fragment.app.viewModels
import com.aoztg.greengrim.R
import com.aoztg.greengrim.databinding.FragmentPaintDetailBinding
import com.aoztg.greengrim.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PaintDetailFragment: BaseFragment<FragmentPaintDetailBinding>(R.layout.fragment_paint_detail) {

    private val viewModel: PaintDetailViewModel by viewModels()
}