package com.aoztg.greengrim.config

import com.aoztg.greengrim.app.App.Companion.sharedPreferences
import com.aoztg.greengrim.data.remote.RefreshAPI
import com.aoztg.greengrim.presentation.util.Constants.BASE_DEV_URL
import com.aoztg.greengrim.presentation.util.Constants.X_ACCESS_TOKEN
import com.aoztg.greengrim.presentation.util.Constants.X_REFRESH_TOKEN
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

class BearerInterceptor : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val response = chain.proceed(originalRequest)

        // API 통신중 특정코드 에러 발생 (accessToken 만료)
        if (response.code == 410) {

            var isRefreshed = false
            var accessToken = ""

            runBlocking {

                // 로컬에 refreshToken이 있다면
                sharedPreferences.getString(X_REFRESH_TOKEN, null)?.let { refresh ->

                    // refresh API 호출
                    val result = Retrofit.Builder()
                        .baseUrl(BASE_DEV_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()
                        .create(RefreshAPI::class.java).refreshToken(refresh)

                    if (result.isSuccessful) {
                        result.body()?.let { body ->

                            // refresh 성공시 로컬에 저장
                            sharedPreferences.edit()
                                .putString(X_ACCESS_TOKEN, body.accessToken)
                                .putString(X_REFRESH_TOKEN, body.refreshToken)
                                .apply()

                            isRefreshed = true
                            accessToken = body.accessToken
                        }
                    }
                }
            }

            if (isRefreshed) {

                // 기존 API 재호출
                val newRequest = originalRequest.newBuilder()
                    .addHeader("Authorization", accessToken)
                    .build()

                return chain.proceed(newRequest)
            }
        }

        // 해당 특정 에러코드가 그대로 내려간다면, LoginActivity로 다시 보내기
        return response
    }
}