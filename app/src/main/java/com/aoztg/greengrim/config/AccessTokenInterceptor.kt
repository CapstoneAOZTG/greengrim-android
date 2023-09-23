package com.aoztg.greengrim.config

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import com.aoztg.greengrim.config.App.Companion.sharedPreferences
import com.aoztg.greengrim.util.Constants.X_ACCESS_EXPIRE
import com.aoztg.greengrim.util.Constants.X_ACCESS_TOKEN
import com.aoztg.greengrim.util.Constants.X_REFRESH_EXPIRE
import com.aoztg.greengrim.util.Constants.X_REFRESH_TOKEN
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class AccessTokenInterceptor(private val context: Context) : Interceptor{


    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val jwt = sharedPreferences.getString(X_ACCESS_TOKEN,"")?:""
        val refreshToken = sharedPreferences.getString(X_REFRESH_TOKEN,"")?:""
        val accessExpire = sharedPreferences.getString(X_ACCESS_EXPIRE,"")?:""
        val refreshExpire = sharedPreferences.getString(X_REFRESH_EXPIRE,"")?:""


        if (jwt.isNotBlank()) {
            if(isDatePassed(accessExpire)){
                if(isDatePassed(refreshExpire)){
                    sessionExpired()
                }else{
                    //TODO refreshToken 으로 accessToken 갱신로직
                    //TODO exmaple : RefreshTokenService(this).refreshJwt(RefreshJwtPostData(refreshToken))
                }

            }else return initHeader(chain)
        }

        return initHeader(chain)
    }

    private fun initHeader(chain : Interceptor.Chain): Response{
        val jwt: String? = sharedPreferences.getString(X_ACCESS_TOKEN, null)
        val builder: Request.Builder = chain.request().newBuilder()
        if(jwt != null){
            builder.addHeader("Bearer-Token", jwt)
        }
        return chain.proceed(builder.build())
    }


    private fun sessionExpired() {
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed(Runnable{
            val toast = Toast.makeText(context, "세션이 만료되었습니다", Toast.LENGTH_SHORT)
            toast.show()
        },0)

        // TODO Login Activity 로 이동
    }


    // 현재날짜가 주어진날짜 보다 지났는지 판별해주는 함수
    private fun isDatePassed(dateStr : String) : Boolean{
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        val date = LocalDateTime.parse(dateStr, formatter)
        val now = LocalDateTime.now()

        return now.isAfter(date)
    }

    // TODO RefershToken 갱신 통신 성공시, 실패시 interface 메소드 구현
    // TODO 성공시 다시 sharedPreferences 에 토큰 새롭게 저장

}