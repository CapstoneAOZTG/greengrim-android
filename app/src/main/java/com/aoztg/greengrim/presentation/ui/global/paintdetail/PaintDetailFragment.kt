package com.aoztg.greengrim.presentation.ui.global.paintdetail

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.aoztg.greengrim.R
import com.aoztg.greengrim.databinding.FragmentPaintDetailBinding
import com.aoztg.greengrim.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PaintDetailFragment: BaseFragment<FragmentPaintDetailBinding>(R.layout.fragment_paint_detail) {

    private val viewModel: PaintDetailViewModel by viewModels()
    private val args: PaintDetailFragmentArgs by navArgs()
    private val grimId by lazy { args.grimId }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.vm = viewModel
        viewModel.setId(grimId)
        initEventObserver()
    }

    private fun initEventObserver(){
        repeatOnStarted {
            viewModel.events.collect{
                when(it) {
                    is PaintDetailEvents.ShowSnackMessage -> showCustomSnack(binding.ivGrim,it.msg)
                    is PaintDetailEvents.NavigateToBack -> findNavController().navigateUp()
                }
            }
        }
    }


}