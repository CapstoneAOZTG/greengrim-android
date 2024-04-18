package com.aoztg.greengrim.presentation.ui.global.nftdetail

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.aoztg.greengrim.R
import com.aoztg.greengrim.databinding.FragmentNftDetailBinding
import com.aoztg.greengrim.presentation.base.BaseFragment
import com.aoztg.greengrim.presentation.ui.main.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NftDetailFragment : BaseFragment<FragmentNftDetailBinding>(R.layout.fragment_nft_detail) {

    private val parentViewModel: MainViewModel by activityViewModels()
    private val viewModel: NftDetailViewModel by viewModels()
    private val args: NftDetailFragmentArgs by navArgs()
    private val nftId by lazy { args.nftId }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        parentViewModel.hideBNV()
        binding.vm = viewModel
        viewModel.setNftId(nftId)
        initEventObserve()
    }

    private fun initEventObserve() {
        repeatOnStarted {
            viewModel.events.collect {
                when (it) {
                    is NftDetailEvents.NavigateToBack -> findNavController().navigateUp()
                    is NftDetailEvents.ShowSnackMessage -> showCustomSnack(binding.ivNft, it.msg)
                    is NftDetailEvents.ShowToastMessage -> showCustomToast(it.msg)
                }
            }
        }
    }


}