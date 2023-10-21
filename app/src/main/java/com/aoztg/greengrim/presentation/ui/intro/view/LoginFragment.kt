package com.aoztg.greengrim.presentation.ui.intro.view

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.aoztg.greengrim.R
import com.aoztg.greengrim.app.App.Companion.gso
import com.aoztg.greengrim.databinding.FragmentLoginBinding
import com.aoztg.greengrim.presentation.base.BaseFragment
import com.aoztg.greengrim.presentation.ui.intro.navigateToTerms
import com.aoztg.greengrim.presentation.ui.intro.viewmodel.IntroViewModel
import com.aoztg.greengrim.presentation.ui.intro.viewmodel.LoginViewModel
import com.aoztg.greengrim.presentation.ui.intro.viewmodel.LoginState
import com.aoztg.greengrim.presentation.util.Constants.TAG
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.NidOAuthLogin
import com.navercorp.nid.oauth.OAuthLoginCallback
import com.navercorp.nid.profile.NidProfileCallback
import com.navercorp.nid.profile.data.NidProfileResponse
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding>(R.layout.fragment_login) {

    private val parentViewModel: IntroViewModel by activityViewModels()
    private val viewModel: LoginViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        super.onCreate(savedInstanceState)
        binding.view = this
        initObserver()
    }

    private fun initObserver() {
        repeatOnStarted {
            viewModel.uiState.collect {
                when (it.loginState) {
                    is LoginState.Success -> {
                        parentViewModel.goToMain()
                    }

                    is LoginState.NoMember -> {
                        findNavController().navigateToTerms()
                    }

                    is LoginState.Error -> {
                        showCustomToast(it.loginState.msg)
                    }

                    else -> {}
                }
            }
        }
    }

    fun googleLogin() {
        val googleSignInClient = GoogleSignIn.getClient(requireContext(), gso)
        val signInIntent = googleSignInClient.signInIntent
        resultLauncher.launch(signInIntent)
    }

    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->

            if (result.resultCode == Activity.RESULT_OK) {
                // 로그인 유저정보 불러오기
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                val account = task.getResult(ApiException::class.java)

                parentViewModel.emailData = account?.email.toString()
                viewModel.startLogin(account?.email.toString())
            }
        }

    fun kakaoLogin() {

        // 카카오톡 설치 확인
        if (UserApiClient.instance.isKakaoTalkLoginAvailable(requireContext())) {

            // 카카오톡 로그인
            UserApiClient.instance.loginWithKakaoTalk(requireContext()) { token, error ->

                // 로그인 실패 부분
                if (error != null) {

                    // 사용자가 취소
                    if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                        return@loginWithKakaoTalk
                    }
                    // 다른 오류
                    else {
                        UserApiClient.instance.loginWithKakaoAccount(
                            requireContext(),
                            callback = kakaoEmailCb
                        ) // 카카오 이메일 로그인
                    }
                }
                // 로그인 성공 부분
                else if (token != null) {
                    kakaoCallInfo()
                }
            }
        } else {
            UserApiClient.instance.loginWithKakaoAccount(
                requireContext(),
                callback = kakaoEmailCb
            ) // 카카오 이메일 로그인
        }
    }

    // 카카오톡 이메일 로그인 콜백
    private val kakaoEmailCb: (OAuthToken?, Throwable?) -> Unit = { token, error ->

        if (error != null) {
            Log.e(TAG, "이메일 로그인 실패 $error")
        } else if (token != null) {
            kakaoCallInfo()
        }
    }

    // 카카오 유저정보 불러오기
    private fun kakaoCallInfo() {
        // 로그인 유저정보 불러오기

        UserApiClient.instance.me { user, error ->
            if (error != null) {
                Log.e(TAG, "사용자 정보 요청 실패 $error")
            } else if (user != null) {
                Log.d(TAG, "사용자 정보 요청 성공 : $user")
                user.kakaoAccount?.email?.let {
                    parentViewModel.emailData = it
                    viewModel.startLogin(it)
                }
            }
        }

    }

    fun naverLogin() {
        val oauthLoginCallback = object : OAuthLoginCallback {

            override fun onSuccess() {
                NidOAuthLogin().callProfileApi(profileCallback)
            }

            override fun onFailure(httpStatus: Int, message: String) {
                val errorCode = NaverIdLoginSDK.getLastErrorCode().code
                val errorDescription = NaverIdLoginSDK.getLastErrorDescription()
            }

            override fun onError(errorCode: Int, message: String) {
                onFailure(errorCode, message)
            }
        }
        NaverIdLoginSDK.authenticate(requireContext(), oauthLoginCallback)
    }

    // 네이버 유저정보 콜백
    private val profileCallback = object : NidProfileCallback<NidProfileResponse> {
        override fun onSuccess(result: NidProfileResponse) {
            Log.d(TAG, result.profile.toString())

            parentViewModel.emailData = result.profile?.email.toString()
            viewModel.startLogin(result.profile?.email.toString())
        }

        override fun onFailure(httpStatus: Int, message: String) {
            val errorCode = NaverIdLoginSDK.getLastErrorCode().code
            val errorDescription = NaverIdLoginSDK.getLastErrorDescription()
        }

        override fun onError(errorCode: Int, message: String) {
            onFailure(errorCode, message)
        }
    }

}