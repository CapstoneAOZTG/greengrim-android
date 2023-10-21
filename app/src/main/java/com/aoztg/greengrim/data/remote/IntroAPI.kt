package com.aoztg.greengrim.data.remote

import com.aoztg.greengrim.data.model.LoginRequest
import com.aoztg.greengrim.data.model.LoginResponse
import com.aoztg.greengrim.data.model.SignupRequest
import com.aoztg.greengrim.data.model.SignupResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import javax.inject.Singleton

@Singleton
interface IntroAPI {
    @POST("/sign-up")
    suspend fun signup(
        @Body params: SignupRequest
    ): Response<SignupResponse>

    @POST("/login")
    suspend fun login(
        @Body params : LoginRequest
    ) : Response<LoginResponse>
}