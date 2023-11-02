package com.aoztg.greengrim.presentation.ui.info.info

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.aoztg.greengrim.R
import com.aoztg.greengrim.app.App
import com.aoztg.greengrim.app.App.Companion.sharedPreferences
import com.aoztg.greengrim.databinding.FragmentInfoBinding
import com.aoztg.greengrim.presentation.base.BaseFragment
import com.aoztg.greengrim.presentation.ui.intro.IntroActivity
import com.aoztg.greengrim.presentation.ui.main.MainViewModel
import com.aoztg.greengrim.presentation.util.Constants
import com.aoztg.greengrim.presentation.util.getInfoSettingSheet
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InfoFragment : BaseFragment<FragmentInfoBinding>(R.layout.fragment_info) {

    private val parentViewModel: MainViewModel by activityViewModels()
    private val viewModel: InfoViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        parentViewModel.showBNV()
        binding.vm = viewModel
        initEventObserver()
    }

    private fun initEventObserver() {
        repeatOnStarted {
            viewModel.events.collect {
                when (it) {
                    is InfoEvents.ShowBottomSheet -> showBottomSheet()
                }
            }
        }
    }

    private fun showBottomSheet() {
        getInfoSettingSheet(
            requireContext(),
            onEditProfileClickListener = ::navigateToEditProfile,
            onNotificationClickListener = ::navigateToNotification,
            onEditPasswordClickListener = ::navigateToEditPassword,
            onAnnouncementClickListener = ::navigateToAnnouncement,
            onTermsClickListener = ::navigateToTerms,
            onAppVersionClickListener = ::navigateToAppVersion,
            onLogoutClickListener = ::logout,
            onWithdrawalClickListener = ::withdrawal,
        ).show()
    }

    private fun navigateToEditProfile() {
        val action = InfoFragmentDirections.actionInfoFragmentToEditProfileFragment()
        findNavController().navigate(action)
    }

    private fun navigateToNotification() {
        showCustomToast("알림으로 이동")
    }

    private fun navigateToEditPassword() {
        showCustomToast("패스워드 변경으로 이동")
    }

    private fun navigateToAnnouncement() {
        showCustomToast("공지사항으로 이동")
    }

    private fun navigateToTerms() {
        showCustomToast("이용 약관으로 이동")
    }

    private fun navigateToAppVersion() {
        showCustomToast("앱 버전으로 이동")
    }

    private fun logout() {
        parentViewModel.logout()
    }

    private fun withdrawal() {
        showCustomToast("회원탈퇴")
    }
}