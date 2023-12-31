package com.aoztg.greengrim.presentation.ui.info.mywallet

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.aoztg.greengrim.R
import com.aoztg.greengrim.databinding.FragmentMyWalletBinding
import com.aoztg.greengrim.presentation.base.BaseFragment
import com.aoztg.greengrim.presentation.ui.main.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyWalletFragment : BaseFragment<FragmentMyWalletBinding>(R.layout.fragment_my_wallet) {

    private val viewModel: MyWalletViewModel by viewModels()
    private val parentViewModel: MainViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        parentViewModel.hideBNV()
        binding.vm = viewModel
        initEventsObserver()
    }

    override fun onResume() {
        super.onResume()
        viewModel.getMyWalletInfo()
    }

    private fun initEventsObserver() {
        repeatOnStarted {
            viewModel.events.collect {
                when (it) {
                    is MyWalletEvents.CopyClipBoard -> parentViewModel.copyInClipBoard(it.link)
                    is MyWalletEvents.ShowSnackMessage -> showCustomSnack(
                        binding.tvMyAmountLabel,
                        it.msg
                    )

                    is MyWalletEvents.ShowToastMessage -> showCustomToast(it.msg)
                    is MyWalletEvents.NavigateToBack -> findNavController().navigateUp()
                    is MyWalletEvents.ShowLoading -> showLoading(requireContext())
                    is MyWalletEvents.DismissLoading -> dismissLoading()
                }
            }
        }
    }
}