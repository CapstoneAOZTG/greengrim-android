package com.aoztg.greengrim.presentation.ui.mypage

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.aoztg.greengrim.R
import com.aoztg.greengrim.app.App
import com.aoztg.greengrim.databinding.FragmentMySettingBinding
import com.aoztg.greengrim.presentation.base.BaseFragment
import com.aoztg.greengrim.presentation.ui.intro.IntroActivity
import com.aoztg.greengrim.presentation.ui.main.MainViewModel
import com.aoztg.greengrim.presentation.util.Constants
import com.aoztg.greengrim.presentation.util.Constants.TAG
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.kakao.sdk.user.UserApiClient
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.NidOAuthLogin
import com.navercorp.nid.oauth.OAuthLoginCallback
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MySettingFragment : BaseFragment<FragmentMySettingBinding>(R.layout.fragment_my_setting) {

    private val viewModel: MySettingViewModel by viewModels()
    private val parentViewModel: MainViewModel by activityViewModels()
    private val args: MySettingFragmentArgs by navArgs()
    private val hasWallet by lazy { args.hasWallet }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        parentViewModel.showBNV()
        binding.vm = viewModel
        initEventObserve()
        setBtnListener()
    }

    private fun initEventObserve() {
        repeatOnStarted {
            viewModel.event.collect {
                when (it) {
                    is MySettingEvent.WithDraw -> withDraw(it.type)
                    is MySettingEvent.Logout -> logout(it.type)
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
                    is MySettingEvent.ShowSnackMessage -> showCustomSnack(
                        binding.tvInfoLabel,
                        it.msg
                    )
                }
            }
        }
    }

    private fun setBtnListener() {
        with(binding) {
            tvLogout.setOnClickListener {
                showTwoButtonTitleDialog(
                    requireContext(),
                    "로그아웃 하시겠습니까?", "취소하기", "로그아웃"
                ) {
                    viewModel.logout()
                }
            }

            btnLogout.setOnClickListener {
                showTwoButtonTitleDialog(
                    requireContext(),
                    "로그아웃 하시겠습니까?", "취소하기", "로그아웃"
                ) {
                    viewModel.logout()
                }
            }

            tvWithdrawal.setOnClickListener {
                showTwoButtonTitleDialog(
                    requireContext(),
                    "정말 탈퇴하시겠습니까?", "취소하기", "탈퇴하기"
                ) {
                    viewModel.withDraw()
                }
            }

            btnWithdrawal.setOnClickListener {
                showTwoButtonTitleDialog(
                    requireContext(),
                    "정말 탈퇴하시겠습니까?", "취소하기", "탈퇴하기"
                ) {
                    viewModel.withDraw()
                }
            }
        }
    }

    private fun withDraw(socialType: String) {
        showCustomToast("회원탈퇴 성공")
        when (socialType) {
            Constants.KAKAO -> kakaoUnlink()
            Constants.NAVER -> naverUnlink()
            Constants.GOOGLE -> googleLogout()
            else -> kakaoUnlink()
        }
    }

    private fun logout(socialType: String) {
        showCustomToast("로그아웃 성공")
        when (socialType) {
            Constants.KAKAO -> kakaoLogout()
            Constants.NAVER -> naverLogout()
            Constants.GOOGLE -> googleLogout()
            else -> kakaoUnlink()
        }
    }

    // 구글 로그아웃
    private fun googleLogout() {
        val googleSignInClient = GoogleSignIn.getClient(
            requireActivity(), GoogleSignInOptions.Builder(
                GoogleSignInOptions.DEFAULT_SIGN_IN
            ).build()
        )
        googleSignInClient.signOut().addOnCompleteListener {
            goToIntro()
        }
    }

    // 카카오 로그아웃
    private fun kakaoLogout() {
        UserApiClient.instance.logout { error ->
            if (error != null) {
                Log.e(TAG, "로그아웃 실패. SDK에서 토큰 삭제됨", error)
            } else {
                Log.d(TAG, "로그아웃 성공. SDK에서 토큰 삭제됨")
                goToIntro()
            }
        }
    }

    // 카카오 연결 끊기
    private fun kakaoUnlink() {

        UserApiClient.instance.unlink { error ->
            if (error != null) {
                Log.e(TAG, "연결 끊기 실패", error)
            } else {
                Log.d(TAG, "연결 끊기 성공. SDK에서 토큰 삭제 됨")
                goToIntro()
            }
        }
    }

    // 네이버 로그아웃
    private fun naverLogout() {
        NaverIdLoginSDK.logout()
        goToIntro()
    }

    // 네이버 연결끊기
    private fun naverUnlink() {
        NidOAuthLogin().callDeleteTokenApi(requireContext(), object : OAuthLoginCallback {
            override fun onSuccess() {
                //서버에서 토큰 삭제에 성공한 상태입니다.
                goToIntro()
            }

            override fun onFailure(httpStatus: Int, message: String) {
                // 서버에서 토큰 삭제에 실패했어도 클라이언트에 있는 토큰은 삭제되어 로그아웃된 상태입니다.
                // 클라이언트에 토큰 정보가 없기 때문에 추가로 처리할 수 있는 작업은 없습니다.
                Log.d("naver", "errorCode: ${NaverIdLoginSDK.getLastErrorCode().code}")
                Log.d("naver", "errorDesc: ${NaverIdLoginSDK.getLastErrorDescription()}")
            }

            override fun onError(errorCode: Int, message: String) {
                // 서버에서 토큰 삭제에 실패했어도 클라이언트에 있는 토큰은 삭제되어 로그아웃된 상태입니다.
                // 클라이언트에 토큰 정보가 없기 때문에 추가로 처리할 수 있는 작업은 없습니다.
                onFailure(errorCode, message)
            }
        })
    }

    private fun goToIntro() {

        App.sharedPreferences.edit()
            .clear()
            .apply()

        val intent = Intent(requireContext(), IntroActivity::class.java)
            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
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