package com.aoztg.greengrim.data.config

import android.content.Intent
import android.util.Log
import com.aoztg.greengrim.BuildConfig
import com.aoztg.greengrim.app.App.Companion.context
import com.aoztg.greengrim.data.model.BaseState
import com.aoztg.greengrim.data.model.response.LoginResponse
import com.aoztg.greengrim.data.model.runRemote
import com.aoztg.greengrim.data.remote.MemberAPI
import com.aoztg.greengrim.presentation.ui.intro.IntroActivity
import com.aoztg.greengrim.presentation.util.Constants.TAG
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import javax.inject.Inject

class BearerInterceptor @Inject constructor(private val keyDataStoreManager: KeyDataStoreManager) :
    Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val response = chain.proceed(originalRequest)

        // API 통신중 특정코드 에러 발생 (accessToken 만료)
        if (response.code == 410) {

            Log.d(TAG, "갱신시작")
            var isRefreshed = false
            var accessToken = ""

            runBlocking {

                // 로컬에 refreshToken이 있다면
                keyDataStoreManager.getRefreshToken()?.let {
                    when (val result = getNewAccessToken(it)) {
                        is BaseState.Success -> {
                            // refresh 성공시 로컬에 저장
                            keyDataStoreManager.putAccessToken(result.body.accessToken)
                            keyDataStoreManager.putRefreshToken(result.body.refreshToken)
                            keyDataStoreManager.putMemberId(result.body.memberId)

                            isRefreshed = true
                            accessToken = result.body.accessToken
                        }

                        is BaseState.Error -> {
                            Log.d(TAG, result.msg)
                        }
                    }
                }
            }

            if (isRefreshed) {

                // 기존 API 재호출
                val newRequest = originalRequest.newBuilder()
                    .addHeader("Authorization", accessToken)
                    .build()

                response.close()

                return chain.proceed(newRequest)
            } else {
                // 해당 특정 에러코드가 그대로 내려간다면, IntroActivity로 이동. 세션 만료 처리
                runBlocking {
                    keyDataStoreManager.deleteAccessToken()
                    keyDataStoreManager.deleteRefreshToken()
                    keyDataStoreManager.deleteMemberId()
                    keyDataStoreManager.deleteSocialType()
                }

                val intent = Intent(context(), IntroActivity::class.java)
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                context().startActivity(intent)
            }
        }

        // 해당 특정 에러코드가 그대로 내려간다면, LoginActivity로 다시 보내기
        return response
    }

    private suspend fun getNewAccessToken(refreshToken: String): BaseState<LoginResponse> {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        val okHttpClient = OkHttpClient.Builder().addInterceptor(loggingInterceptor).build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_DEV_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
        val api = retrofit.create(MemberAPI::class.java)
        return runRemote {
            api.refreshToken(
                refreshToken
            )
        }
    }
}