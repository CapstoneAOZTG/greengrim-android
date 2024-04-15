package com.aoztg.greengrim.presentation.ui.mypage.mywallet

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.aoztg.greengrim.R
import com.aoztg.greengrim.databinding.FragmentAddWalletBinding
import com.aoztg.greengrim.presentation.base.BaseFragment
import com.aoztg.greengrim.presentation.ui.main.MainViewModel
import com.aoztg.greengrim.presentation.ui.toWebView
import com.aoztg.greengrim.presentation.util.Constants

class AddWalletFragment: BaseFragment<FragmentAddWalletBinding>(R.layout.fragment_add_wallet) {

    private val parentViewModel : MainViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        parentViewModel.hideBNV()
        with(binding){
            btnBack.setOnClickListener {
                findNavController().navigateUp()
            }

            btnNavigateToClip.setOnClickListener {
                findNavController().toWebView(Constants.clipUrl)
            }

            btnNavigateToAddDetail.setOnClickListener {
                findNavController().toAddDetailWallet()
            }
        }
    }

    private fun NavController.toAddDetailWallet(){
        val action = AddWalletFragmentDirections.actionAddWalletFragmentToAddWalletDetailFragment()
        navigate(action)
    }
}