package com.aoztg.greengrim.app

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.aoztg.greengrim.BuildConfig
import com.aoztg.greengrim.R
import com.aoztg.greengrim.config.MyFirebaseMessagingService
import com.aoztg.greengrim.presentation.util.Constants.TAG
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.FirebaseApp
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.util.Utility
import com.navercorp.nid.NaverIdLoginSDK
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


@HiltAndroidApp
class App : Application() {


    //  앱의 context 를 instance 변수에 저장
    init{
        instance =this
    }
    companion object{
        lateinit var instance : App
        lateinit var sharedPreferences: SharedPreferences
        lateinit var gso : GoogleSignInOptions
        var fcmToken = ""

        // 앱의 context 를 불러오는 함수
        fun context() : Context {
            return instance.applicationContext
        }
    }

    override fun onCreate() {
        super.onCreate()
        sharedPreferences =
            applicationContext.getSharedPreferences("APP", MODE_PRIVATE)
        initSocialLogin()
        getFCMToken()
    }

    private fun initSocialLogin(){
        Log.d(TAG, "keyhash : ${Utility.getKeyHash(this)}")

        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestProfile()
            .requestEmail()
            .build()

        KakaoSdk.init(this, BuildConfig.KAKAO_API_KEY)
        NaverIdLoginSDK.initialize(this,
            BuildConfig.NAVER_CLIENT_ID,
            BuildConfig.NAVER_CLIENT_SECRET,
            BuildConfig.NAVER_CLIENT_NAME
        )
    }

    private fun getFCMToken() {

        FirebaseApp.initializeApp(this@App)
        CoroutineScope(Dispatchers.Main).launch {
            fcmToken = async { MyFirebaseMessagingService().getFirebaseToken() }.await()
            Log.d(TAG, fcmToken)
        }
    }
}