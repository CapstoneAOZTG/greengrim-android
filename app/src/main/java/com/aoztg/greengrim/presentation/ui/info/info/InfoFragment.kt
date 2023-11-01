package com.aoztg.greengrim.presentation.ui.info.info

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.aoztg.greengrim.R
import com.aoztg.greengrim.databinding.FragmentInfoBinding
import com.aoztg.greengrim.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InfoFragment : BaseFragment<FragmentInfoBinding>(R.layout.fragment_info) {

    private val viewModel: InfoViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.vm = viewModel
    }
}