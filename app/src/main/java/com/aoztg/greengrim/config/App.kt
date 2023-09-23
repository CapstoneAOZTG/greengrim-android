package com.aoztg.greengrim.config

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.aoztg.greengrim.util.Constants.BASE_URL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class App : Application() {


    //  앱의 context 를 instance 변수에 저장
    init{
        instance =this
    }

    companion object{
        lateinit var instance : App
        lateinit var sharedPreferences: SharedPreferences
        lateinit var retrofit : Retrofit

        // 앱의 context 를 불러오는 함수
        fun context() : Context {
            return instance.applicationContext
        }

        fun getRetro() : Retrofit{
            return retrofit.newBuilder()
                .baseUrl(BASE_URL)
                .build()
        }
    }

    override fun onCreate() {
        super.onCreate()
        sharedPreferences =
            applicationContext.getSharedPreferences("APP", MODE_PRIVATE)
        initRetrofitInstance()
    }

    private fun initRetrofitInstance() {
        val client: OkHttpClient = OkHttpClient.Builder()
            .readTimeout(30000, TimeUnit.MILLISECONDS)
            .connectTimeout(30000, TimeUnit.MILLISECONDS)
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .addNetworkInterceptor(AccessTokenInterceptor(applicationContext)) // JWT 자동 헤더 전송
            .build()

        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}