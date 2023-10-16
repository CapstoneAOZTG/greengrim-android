package com.aoztg.greengrim.presentation.ui.intro.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.aoztg.greengrim.presentation.App.Companion.gso
import com.aoztg.greengrim.databinding.ActivityLoginBinding
import com.aoztg.greengrim.presentation.base.BaseActivity
import com.aoztg.greengrim.presentation.ui.intro.EmailData
import com.aoztg.greengrim.presentation.ui.intro.event.LoginEvent
import com.aoztg.greengrim.presentation.ui.intro.viewmodel.LoginViewModel
import com.aoztg.greengrim.presentation.ui.main.view.MainActivity
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
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginActivity : BaseActivity<ActivityLoginBinding>(ActivityLoginBinding::inflate) {


    private val viewModel : LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.vm = viewModel
        binding.view = this

        setEventObserver()
    }

    private fun setEventObserver(){
        lifecycleScope.launch {
            viewModel.eventFlow.collectLatest {
                when(it){
                    is LoginEvent.NavigateToMainActivity -> {
                        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                    }

                    is LoginEvent.NavigateToTermsActivity -> {
                        startActivity(Intent(this@LoginActivity, TermsActivity::class.java))
                    }
                }
            }
        }
    }

    fun googleLogin() {
        val googleSignInClient = GoogleSignIn.getClient(this, gso)
        val signInIntent = googleSignInClient.signInIntent
        resultLauncher.launch(signInIntent)
    }

    private val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->

        if(result.resultCode == Activity.RESULT_OK){
            // 로그인 유저정보 불러오기
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            val account = task.getResult(ApiException::class.java)

            EmailData.setEmail(account?.email.toString())
            viewModel.startLogin(account?.idToken.toString())
        }
    }

    fun kakaoLogin(){

        // 카카오톡 설치 확인
        if (UserApiClient.instance.isKakaoTalkLoginAvailable(this)) {

            // 카카오톡 로그인
            UserApiClient.instance.loginWithKakaoTalk(this) { token, error ->

                // 로그인 실패 부분
                if (error != null) {

                    // 사용자가 취소
                    if (error is ClientError && error.reason == ClientErrorCause.Cancelled ) {
                        return@loginWithKakaoTalk
                    }
                    // 다른 오류
                    else {
                        UserApiClient.instance.loginWithKakaoAccount(this, callback = kakaoEmailCb) // 카카오 이메일 로그인
                    }
                }
                // 로그인 성공 부분
                else if (token != null) {
                    viewModel.startLogin(token.accessToken)
                    kakaoCallInfo()
                }
            }
        } else {
            UserApiClient.instance.loginWithKakaoAccount(this, callback = kakaoEmailCb) // 카카오 이메일 로그인
        }
    }

    // 카카오톡 이메일 로그인 콜백
    private val kakaoEmailCb: (OAuthToken?, Throwable?) -> Unit = { token, error ->

        if (error != null) {
            Log.e(TAG, "이메일 로그인 실패 $error")
        } else if (token != null) {
            viewModel.startLogin(token.accessToken)
            kakaoCallInfo()
        }
    }

    // 카카오 유저정보 불러오기
    private fun kakaoCallInfo(){
        // 로그인 유저정보 불러오기
        UserApiClient.instance.me { user, error ->
            error?.let{ e ->
                user?.let{
                    EmailData.setEmail(it.kakaoAccount?.email.toString())
                }
            }
        }
    }

    fun naverLogin(){
        val oauthLoginCallback = object : OAuthLoginCallback {

            override fun onSuccess() {
                viewModel.startLogin(NaverIdLoginSDK.getAccessToken().toString())
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
        NaverIdLoginSDK.authenticate(this, oauthLoginCallback)
    }


    // 네이버 유저정보 콜백
    private val profileCallback = object : NidProfileCallback<NidProfileResponse> {
        override fun onSuccess(response: NidProfileResponse) {
            EmailData.setEmail(response.profile?.email.toString())
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