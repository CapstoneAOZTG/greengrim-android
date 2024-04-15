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
import com.aoztg.greengrim.presentation.customview.SettingBottomSheet
import com.aoztg.greengrim.presentation.ui.SocialLoginType
import com.aoztg.greengrim.presentation.ui.main.MainViewModel
import com.aoztg.greengrim.presentation.ui.toAttendCheck
import com.aoztg.greengrim.presentation.util.Constants.GOOGLE
import com.aoztg.greengrim.presentation.util.Constants.KAKAO
import com.aoztg.greengrim.presentation.util.Constants.NAVER
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.kakao.sdk.user.UserApiClient
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.NidOAuthLogin
import com.navercorp.nid.oauth.OAuthLoginCallback
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyPageFragment : BaseFragment<FragmentMypageBinding>(R.layout.fragment_mypage) {

    private val parentViewModel: MainViewModel by activityViewModels()
    private val viewModel: MyPageViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        parentViewModel.showBNV()
        binding.vm = viewModel
        initEventObserver()
        viewModel.getMyInfo()
    }

    private fun initEventObserver() {
        repeatOnStarted {
            viewModel.events.collect {
                when (it) {
                    is MyPageEvent.ShowBottomSheet -> showBottomSheet()
                    is MyPageEvent.GoToIntroActivity -> goToIntroActivity()
                    is MyPageEvent.NavigateToAttendCheck -> findNavController().toAttendCheck()
                    is MyPageEvent.NavigateToMyChallenge -> findNavController().toMyChallenge()
                    is MyPageEvent.NavigateToMyCertification -> findNavController().toMyCertification()
                    is MyPageEvent.ShowToastMessage -> showCustomToast(it.msg)
                    is MyPageEvent.ShowSnackMessage -> showCustomSnack(binding.ivProfile, it.msg)
                    is MyPageEvent.NavigateToMyWallet -> findNavController().toMyWallet()
                    is MyPageEvent.NavigateToMyNft -> findNavController().toMyNft()
                }
            }
        }
    }

    private fun showBottomSheet() {
        SettingBottomSheet(
            requireContext(),
            onNotificationClickListener = ::navigateToNotification,
            onEditPasswordClickListener = ::navigateToEditPassword,
            onAnnouncementClickListener = ::navigateToAnnouncement,
            onTermsClickListener = ::navigateToTerms,
            onAppVersionClickListener = ::navigateToAppVersion,
            onLogoutClickListener = ::goToIntroActivity,
            onWithdrawalClickListener = ::withdrawal,
        ).show()
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

    private fun goToIntroActivity() {
        when (SocialLoginType.type) {
            KAKAO -> kakaoUnlink()
            NAVER -> naverUnlink()
            GOOGLE -> googleLogout()
        }
        parentViewModel.logout()
    }

    private fun withdrawal() {
        viewModel.withdrawal()
    }

    private fun googleLogout() {
        val googleSignInClient = GoogleSignIn.getClient(
            requireContext(), GoogleSignInOptions.Builder(
                GoogleSignInOptions.DEFAULT_SIGN_IN
            ).build()
        )
        googleSignInClient.signOut().addOnCompleteListener {

        }
    }

    private fun kakaoUnlink() {
        UserApiClient.instance.unlink { error ->
            if (error != null) {
                showCustomToast("로그아웃 실패")
            } else {

            }
        }
    }

    private fun naverUnlink() {
        NidOAuthLogin().callDeleteTokenApi(requireContext(), object : OAuthLoginCallback {
            override fun onSuccess() {
            }

            override fun onFailure(httpStatus: Int, message: String) {
                showCustomToast("errorDesc: ${NaverIdLoginSDK.getLastErrorDescription()}")
            }

            override fun onError(errorCode: Int, message: String) {
                onFailure(errorCode, message)
            }
        })
    }
}