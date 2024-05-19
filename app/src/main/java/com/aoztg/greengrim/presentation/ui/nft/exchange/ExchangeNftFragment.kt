package com.aoztg.greengrim.presentation.ui.nft.exchange

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.aoztg.greengrim.R
import com.aoztg.greengrim.databinding.FragmentExchangeNftBinding
import com.aoztg.greengrim.presentation.base.BaseFragment
import com.aoztg.greengrim.presentation.ui.main.MainViewModel
import com.aoztg.greengrim.presentation.ui.nft.nftcollection.NftCollectionFilter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ExchangeNftFragment :
    BaseFragment<FragmentExchangeNftBinding>(R.layout.fragment_exchange_nft) {

    private val viewModel: ExchangeNftViewModel by viewModels()
    private val parentViewModel: MainViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        parentViewModel.hideBNV()
        binding.vm = viewModel
        initEventObserve()
        initStateObserve()
    }

    private fun initEventObserve() {
        repeatOnStarted {
            viewModel.event.collect {
                when (it) {
                    is ExchangeNftEvent.NavigateToExchangeDetail -> findNavController().toExchangeNftDetail(
                        it.grade
                    )

                    is ExchangeNftEvent.NavigateToBack -> findNavController().navigateUp()
                }
            }
        }
    }

    private fun initStateObserve() {
        repeatOnStarted {
            viewModel.curFilter.collect {
                when (it) {
                    NftCollectionFilter.BASIC -> {
                        binding.layoutBasic.setBackgroundResource(R.drawable.shape_lightblack2fill_whitestroke_radius20)
                        binding.layoutStandard.setBackgroundResource(R.drawable.shape_lightblack2fill_blackstroke_radius20)
                        binding.layoutPremium.setBackgroundResource(R.drawable.shape_lightblack2fill_blackstroke_radius20)
                    }

                    NftCollectionFilter.STANDARD -> {
                        binding.layoutStandard.setBackgroundResource(R.drawable.shape_lightblack2fill_whitestroke_radius20)
                        binding.layoutBasic.setBackgroundResource(R.drawable.shape_lightblack2fill_blackstroke_radius20)
                        binding.layoutPremium.setBackgroundResource(R.drawable.shape_lightblack2fill_blackstroke_radius20)
                    }

                    NftCollectionFilter.PREMIUM -> {
                        binding.layoutPremium.setBackgroundResource(R.drawable.shape_lightblack2fill_whitestroke_radius20)
                        binding.layoutBasic.setBackgroundResource(R.drawable.shape_lightblack2fill_blackstroke_radius20)
                        binding.layoutStandard.setBackgroundResource(R.drawable.shape_lightblack2fill_blackstroke_radius20)
                    }
                }
            }
        }
    }

    private fun NavController.toExchangeNftDetail(grade: String) {
        val action =
            ExchangeNftFragmentDirections.actionExchangeNftFragmentToExchangeNftDetailFragment(grade)
        navigate(action)
    }

}