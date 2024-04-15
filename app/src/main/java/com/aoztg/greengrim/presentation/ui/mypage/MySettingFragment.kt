package com.aoztg.greengrim.presentation.ui.mypage

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.aoztg.greengrim.R
import com.aoztg.greengrim.databinding.FragmentMySettingBinding
import com.aoztg.greengrim.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MySettingFragment : BaseFragment<FragmentMySettingBinding>(R.layout.fragment_my_setting) {

    private val viewModel: MySettingViewModel by viewModels()
    private val args: MySettingFragmentArgs by navArgs()
    private val hasWallet by lazy { args.hasWallet }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = viewModel
        initEventObserve()
    }

    private fun initEventObserve() {
        repeatOnStarted {
            viewModel.event.collect {
                when (it) {
                    is MySettingEvent.WithDraw -> withDraw()
                    is MySettingEvent.Logout -> logout()
                    is MySettingEvent.NavigateToSetWallet -> {
                        if (hasWallet) {
                            findNavController().toEditWallet()
                        } else {
                            findNavController().toAddWallet()
                        }
                    }

                    is MySettingEvent.NavigateToEditProfile -> findNavController().toEditProfile()
                    is MySettingEvent.NavigateToEditAlarm -> findNavController().toEditAlarm()
                    is MySettingEvent.NavigateToBack -> findNavController().navigateUp()
                }
            }
        }
    }

    private fun withDraw() {

    }

    private fun logout() {

    }

    private fun NavController.toEditProfile() {
        val action = MySettingFragmentDirections.actionMySettingFragmentToEditProfileFragment()
        navigate(action)
    }

    private fun NavController.toAddWallet() {
        val action = MySettingFragmentDirections.actionMySettingFragmentToAddWalletFragment()
        navigate(action)
    }

    private fun NavController.toEditWallet() {
        val action = MySettingFragmentDirections.actionMySettingFragmentToEditWalletFragment()
        navigate(action)
    }

    private fun NavController.toEditAlarm() {
        val action = MySettingFragmentDirections.actionMySettingFragmentToEditAlarmFragment()
        navigate(action)
    }
}