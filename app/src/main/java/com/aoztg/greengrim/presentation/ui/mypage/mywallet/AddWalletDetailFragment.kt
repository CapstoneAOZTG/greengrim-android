package com.aoztg.greengrim.presentation.ui.mypage.mywallet

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.aoztg.greengrim.R
import com.aoztg.greengrim.databinding.FragmentAddWalletDetailBinding
import com.aoztg.greengrim.presentation.base.BaseFragment
import com.aoztg.greengrim.presentation.ui.main.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddWalletDetailFragment: BaseFragment<FragmentAddWalletDetailBinding>(R.layout.fragment_add_wallet_detail) {

    private val parentViewModel : MainViewModel by activityViewModels()
    private val viewModel : AddWalletDetailViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        parentViewModel.hideBNV()
        binding.vm = viewModel
    }
}