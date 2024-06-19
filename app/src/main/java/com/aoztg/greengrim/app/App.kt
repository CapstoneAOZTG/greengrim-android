package com.aoztg.greengrim.app

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import com.aoztg.greengrim.BuildConfig
import com.aoztg.greengrim.R
import com.aoztg.greengrim.app.Constants.APP_NAME
import com.aoztg.greengrim.presentation.util.Constants.TAG
import com.aoztg.greengrim.service.MyFirebaseMessagingService
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
class App : Application(), LifecycleEventObserver {

    private val lifecycle by lazy { ProcessLifecycleOwner.get().lifecycle }

    //  앱의 context 를 instance 변수에 저장
    init {
        instance = this
    }

    companion object {
        lateinit var instance: App
        lateinit var sharedPreferences: SharedPreferences
        lateinit var gso: GoogleSignInOptions
        val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name =APP_NAME)
        var fcmToken = ""
        var isForeground = false

        // 앱의 context 를 불러오는 함수
        fun context(): Context {
            return instance.applicationContext
        }
    }

    override fun onCreate() {
        super.onCreate()

        sharedPreferences =
            applicationContext.getSharedPreferences("APP", MODE_PRIVATE)
        initSocialLogin()
        getFCMToken()
        lifecycle.addObserver(this)
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        when (event) {
            Lifecycle.Event.ON_STOP -> {
                isForeground = false
                Log.d(TAG, "앱이 백그라운드로 전환")
            }

            Lifecycle.Event.ON_START -> {
                isForeground = true
                Log.d(TAG, "앱이 포그라운드로 전환")
            }

            else -> {}
        }
    }

    private fun initSocialLogin() {
        Log.d(TAG, "keyhash : ${Utility.getKeyHash(this)}")

        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestProfile()
            .requestEmail()
            .build()

        KakaoSdk.init(this, BuildConfig.KAKAO_API_KEY)
        NaverIdLoginSDK.initialize(
            this,
            BuildConfig.NAVER_CLIENT_ID,
            BuildConfig.NAVER_CLIENT_SECRET,
            BuildConfig.NAVER_CLIENT_NAME
        )
    }

    private fun getFCMToken() {

        FirebaseApp.initializeApp(this@App)
        CoroutineScope(Dispatchers.Main).launch {
            fcmToken = async { MyFirebaseMessagingService().getFirebaseToken() }.await()
            Log.d("fcmToken", fcmToken)
        }
    }

}