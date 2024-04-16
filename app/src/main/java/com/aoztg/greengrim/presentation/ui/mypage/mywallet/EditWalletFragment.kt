package com.aoztg.greengrim.presentation.ui.mypage.mywallet

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.aoztg.greengrim.R
import com.aoztg.greengrim.databinding.FragmentEditWalletBinding
import com.aoztg.greengrim.presentation.base.BaseFragment
import com.aoztg.greengrim.presentation.ui.main.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditWalletFragment: BaseFragment<FragmentEditWalletBinding>(R.layout.fragment_edit_wallet) {

    private val parentViewModel : MainViewModel by activityViewModels()
    private val viewModel : EditWalletViewModel by viewModels()



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        parentViewModel.hideBNV()
        viewModel.getWalletInfo()
        binding.vm = viewModel
        initEventObserve()
    }

    private fun initEventObserve(){
        repeatOnStarted {
            viewModel.event.collect{
                when(it){
                    is EditWalletEvent.NavigateToBack -> findNavController().navigateUp()
                    is EditWalletEvent.NavigateToMyPage -> findNavController().toMyPage()
                    is EditWalletEvent.ShowCustomSnack -> showCustomSnack(binding.tvWalletNameLabel, it.msg)
                    is EditWalletEvent.ShowToastMessage -> showCustomToast(it.msg)
                }
            }
        }
    }

    private fun NavController.toMyPage() {
        val action = EditWalletFragmentDirections.actionEditWalletFragmentToMyPageFragment()
        navigate(action)
    }
}