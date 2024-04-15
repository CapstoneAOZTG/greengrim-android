package com.aoztg.greengrim.presentation.ui.mypage

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.aoztg.greengrim.R
import com.aoztg.greengrim.databinding.FragmentMypageBinding
import com.aoztg.greengrim.presentation.base.BaseFragment
import com.aoztg.greengrim.presentation.ui.main.MainViewModel
import com.aoztg.greengrim.presentation.ui.toAttendCheck
import com.aoztg.greengrim.presentation.ui.toWebView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyPageFragment : BaseFragment<FragmentMypageBinding>(R.layout.fragment_mypage) {

    private val parentViewModel: MainViewModel by activityViewModels()
    private val viewModel: MyPageViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        parentViewModel.showBNV()
        binding.vm = viewModel
        initEventObserver()
        viewModel.getMyInfo()
        viewModel.getMyWalletInfo()
    }

    private fun initEventObserver() {
        repeatOnStarted {
            viewModel.events.collect {
                when (it) {
                    is MyPageEvent.NavigateToAttendCheck -> findNavController().toAttendCheck()
                    is MyPageEvent.ShowToastMessage -> showCustomToast(it.msg)
                    is MyPageEvent.ShowSnackMessage -> showCustomSnack(binding.ivProfile, it.msg)
                    is MyPageEvent.NavigateToAddWallet -> findNavController().toAddWallet()
                    is MyPageEvent.NavigateToEditWallet -> findNavController().toEditWallet()
                    is MyPageEvent.NavigateToMyPoint -> findNavController().toMyPoint()
                    is MyPageEvent.NavigateToWebView -> findNavController().toWebView(it.url)
                    is MyPageEvent.NavigateToMyProfile -> findNavController().toMyProfile()
                    is MyPageEvent.NavigateToMySetting -> findNavController().toMySetting(it.hasWallet)
                    is MyPageEvent.ShowLoading -> showLoading(requireContext())
                    is MyPageEvent.DismissLoading -> dismissLoading()
                }
            }
        }
    }

    private fun NavController.toAddWallet() {
        val action = MyPageFragmentDirections.actionMyPageFragmentToAddWalletFragment()
        navigate(action)
    }

    private fun NavController.toEditWallet(){
        val action = MyPageFragmentDirections.actionMyPageFragmentToEditWalletFragment()
        navigate(action)
    }

    private fun NavController.toMyPoint(){
        val action = MyPageFragmentDirections.actionMyPageFragmentToMyPointFragment()
        navigate(action)
    }

    private fun NavController.toMyProfile(){
        val action = MyPageFragmentDirections.actionMyPageFragmentToMyProfileFragment()
        navigate(action)
    }
    private fun NavController.toMySetting(hasWallet: Boolean){
        val action = MyPageFragmentDirections.actionMyPageFragmentToMySettingFragment(hasWallet)
        navigate(action)
    }
}