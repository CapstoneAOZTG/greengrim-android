package com.aoztg.greengrim.presentation.ui.nft.exchange.detail

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.aoztg.greengrim.R
import com.aoztg.greengrim.databinding.FragmentExchangeNftDetailBinding
import com.aoztg.greengrim.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ExchangeNftDetailFragment : BaseFragment<FragmentExchangeNftDetailBinding>(R.layout.fragment_exchange_nft_detail) {

    private val args : ExchangeNftDetailFragmentArgs by navArgs()
    private val grade by lazy { args.grade }

    private val viewModel : ExchangeNftDetailViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.vm = viewModel
        viewModel.setGrade(grade)
    }
}