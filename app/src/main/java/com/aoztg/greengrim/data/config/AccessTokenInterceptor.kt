package com.aoztg.greengrim.data.config

import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import javax.inject.Inject

class AccessTokenInterceptor @Inject constructor(private val keyDataStoreManager: KeyDataStoreManager) :
    Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder: Request.Builder = chain.request().newBuilder()

        val accessToken = runBlocking {
            keyDataStoreManager.getAccessToken()
        }

        accessToken?.let {
            builder.addHeader("Authorization", it)
        }

        return chain.proceed(builder.build())
    }
}
