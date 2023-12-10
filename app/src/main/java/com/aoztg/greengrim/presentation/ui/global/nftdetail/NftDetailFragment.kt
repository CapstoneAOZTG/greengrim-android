package com.aoztg.greengrim.presentation.ui.global.nftdetail

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.aoztg.greengrim.R
import com.aoztg.greengrim.databinding.FragmentNftDetailBinding
import com.aoztg.greengrim.presentation.base.BaseFragment
import com.aoztg.greengrim.presentation.ui.global.model.NftState
import com.aoztg.greengrim.presentation.ui.main.MainViewModel
import com.aoztg.greengrim.presentation.util.Constants.TAG
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NftDetailFragment : BaseFragment<FragmentNftDetailBinding>(R.layout.fragment_nft_detail) {

    private val parentViewModel: MainViewModel by activityViewModels()
    private val viewModel: NftDetailViewModel by viewModels()
    private val args: NftDetailFragmentArgs by navArgs()
    private val nftId by lazy { args.nftId }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.vm = viewModel
        parentViewModel.hideBNV()
        viewModel.setNftId(nftId)
        initStateObserver()
    }

    private fun initStateObserver() {
        repeatOnStarted {
            viewModel.uiState.collect {
                when (it.nftDetail.btnState) {
                    NftState.CAN_SELL -> {
                        with(binding.btnNext) {
                            visibility = View.VISIBLE
                            text = "판매하기"
                            setOnClickListener {
                                findNavController().toNftSell(nftId)
                            }
                        }
                    }

                    NftState.CAN_BUY -> {
                        with(binding.btnNext) {
                            visibility = View.VISIBLE
                            text = "구매하기"
                            setOnClickListener {
                                findNavController().toNftPurchase(nftId)
                            }
                        }
                    }

                    else -> {}
                }
            }
        }
    }

    private fun NavController.toNftSell(nftId: Int) {
        val action = NftDetailFragmentDirections.actionNftDetailFragmentToSellNftFragment(nftId)
        navigate(action)
    }

    private fun NavController.toNftPurchase(nftId: Int) {
        val action = NftDetailFragmentDirections.actionNftDetailFragmentToPurchaseNftFragment(nftId)
        navigate(action)
    }
}