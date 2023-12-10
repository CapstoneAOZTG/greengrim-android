package com.aoztg.greengrim.presentation.ui.global.nftdetail.transaction.sell

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.aoztg.greengrim.R
import com.aoztg.greengrim.data.model.request.SellNftRequest
import com.aoztg.greengrim.databinding.FragmentSellNftBinding
import com.aoztg.greengrim.presentation.base.BaseFragment
import com.aoztg.greengrim.presentation.ui.global.checkpassword.FormBeforePasswordInput
import com.aoztg.greengrim.presentation.ui.toCheckPassword
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SellNftFragment : BaseFragment<FragmentSellNftBinding>(R.layout.fragment_sell_nft) {

    private val viewModel: SellNftViewModel by viewModels()
    private val args: SellNftFragmentArgs by navArgs()
    private val nftId by lazy { args.nftId }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = viewModel
        viewModel.setNftId(nftId)
        initEventObserver()
    }

    private fun initEventObserver() {
        repeatOnStarted {
            viewModel.events.collect {
                when (it) {
                    is SellNftEvents.SellNft -> sellNft(it.price)
                }
            }
        }
    }

    private fun sellNft(price: Double) {
        FormBeforePasswordInput.sellNft(
            SellNftRequest(
                payPassword = "",
                nftId = nftId,
                price = price
            )
        )
        findNavController().toCheckPassword()
    }


}