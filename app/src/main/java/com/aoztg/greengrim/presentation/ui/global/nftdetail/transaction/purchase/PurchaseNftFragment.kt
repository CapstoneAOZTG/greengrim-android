package com.aoztg.greengrim.presentation.ui.global.nftdetail.transaction.purchase

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.aoztg.greengrim.R
import com.aoztg.greengrim.data.model.request.PurchaseNftRequest
import com.aoztg.greengrim.databinding.FragmentPurchaseNftBinding
import com.aoztg.greengrim.presentation.base.BaseFragment
import com.aoztg.greengrim.presentation.ui.global.checkpassword.FormBeforePasswordInput
import com.aoztg.greengrim.presentation.ui.toCheckPassword
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PurchaseNftFragment: BaseFragment<FragmentPurchaseNftBinding>(R.layout.fragment_purchase_nft) {

    private val viewModel: PurchaseNftViewModel by viewModels()
    private val args: PurchaseNftFragmentArgs by navArgs()
    private val nftId by lazy { args.nftId }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.vm = viewModel
        viewModel.setNftId(nftId)
        initEventObserver()
    }

    private fun initEventObserver(){
        repeatOnStarted {
            viewModel.events.collect{
                when(it){
                    is PurchaseNftEvents.PurchaseNft -> purchaseNft(it.marketId)
                    is PurchaseNftEvents.ShowSnackMessage -> showCustomSnack(binding.ivTitleImg, it.msg)
                }
            }
        }
    }

    private fun purchaseNft(marketId: Long){
        FormBeforePasswordInput.purchaseNft(
            PurchaseNftRequest(
                "",
                marketId =  marketId
            )
        )

        findNavController().toCheckPassword()
    }


}