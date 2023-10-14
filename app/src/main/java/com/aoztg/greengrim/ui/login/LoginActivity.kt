package com.aoztg.greengrim.ui.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.lifecycle.ViewModel
import com.aoztg.greengrim.App.Companion.gso
import com.aoztg.greengrim.databinding.ActivityLoginBinding
import com.aoztg.greengrim.ui.base.BaseActivity
import com.aoztg.greengrim.util.Constants.TAG
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.OAuthLoginCallback
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : BaseActivity<ActivityLoginBinding>(ActivityLoginBinding::inflate) {


    private val viewModel : ViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    fun googleLogin (){
        val googleSignInClient = GoogleSignIn.getClient(this, gso)
        val signInIntent = googleSignInClient.signInIntent
        resultLauncher.launch(signInIntent)
    }

    private val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->

        if(result.resultCode == Activity.RESULT_OK){
            // 로그인 유저정보 불러오기
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            val account = task.getResult(ApiException::class.java)

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
            Log.d(TAG, "이메일 로그인 성공 ${token.accessToken}")
        }
    }

    fun naverLogin(){
        val oauthLoginCallback = object : OAuthLoginCallback {

            override fun onSuccess() {
                // 네이버 로그인 인증이 성공했을 때 수행할 코드 추가
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
}