package com.aoztg.greengrim.presentation.ui.nft.exchange.detail

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.aoztg.greengrim.R
import com.aoztg.greengrim.databinding.FragmentExchangeNftDetailBinding
import com.aoztg.greengrim.presentation.base.BaseFragment
import com.aoztg.greengrim.presentation.customview.ExchangeNftDialog
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
        initEventObserve()
    }

    private fun initEventObserve(){
        repeatOnStarted {
            viewModel.event.collect{
                when(it){
                    is ExchangeNftDetailEvent.ShowLoading -> showLoading(requireContext())
                    is ExchangeNftDetailEvent.DismissLoading -> dismissLoading()
                    is ExchangeNftDetailEvent.ShowCustomSnack -> showCustomSnack(binding.tvDescription, it.msg)
                    is ExchangeNftDetailEvent.ShowToastMessage -> showCustomToast(it.msg)
                    is ExchangeNftDetailEvent.ShowExchangeDialog -> showExchangeNftDialog(it.point)
                    is ExchangeNftDetailEvent.NavigateToBack -> findNavController().navigateUp()
                }
            }
        }
    }

    private fun showExchangeNftDialog(point : Int){
        ExchangeNftDialog(
            requireContext(),
            point
        ){
            viewModel.exchangeNft()
        }.show()
    }

    private fun NavController.toEditExchangedNft(imgUrl: String, nftId : Long){
        val action = ExchangeNftDetailFragmentDirections.actionExchangeNftDetailFragmentToEditExchangedNftFragment(imgUrl, nftId)
        navigate(action)
    }
}