package com.aoztg.greengrim.config

import com.aoztg.greengrim.app.App.Companion.sharedPreferences
import com.aoztg.greengrim.presentation.util.Constants.X_ACCESS_TOKEN
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

class RetrofitInterceptor() : Interceptor{

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder: Request.Builder = chain.request().newBuilder()
        val jwt: String? = sharedPreferences.getString(X_ACCESS_TOKEN, null)
        jwt?.let{
            builder.addHeader("Authorization", jwt)
        }?:run{
            // 로컬에 저장된 토큰 없는경우
        }

        return chain.proceed(builder.build())
    }
}
