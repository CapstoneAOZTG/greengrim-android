package com.aoztg.greengrim.data.remote

import com.aoztg.greengrim.data.model.SignupPostData
import retrofit2.http.Body
import retrofit2.http.POST
import javax.inject.Singleton

@Singleton
interface IntroAPI {


    @POST("/signup")
    suspend fun postSignup(
        @Body params : SignupPostData
    ) : Boolean
}