package com.aoztg.greengrim.network.loginnetwork

import com.aoztg.greengrim.network.loginnetwork.model.SignupPostData
import retrofit2.http.Body
import retrofit2.http.POST
import javax.inject.Singleton

@Singleton
interface LoginAPI {


    @POST("/signup")
    suspend fun postSignup(
        @Body params : SignupPostData
    ) : Boolean
}