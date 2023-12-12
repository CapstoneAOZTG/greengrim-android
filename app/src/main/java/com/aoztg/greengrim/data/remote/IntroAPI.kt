package com.aoztg.greengrim.data.remote

import com.aoztg.greengrim.data.model.request.CheckNickRequest
import com.aoztg.greengrim.data.model.response.CheckNickResponse
import com.aoztg.greengrim.data.model.request.LoginRequest
import com.aoztg.greengrim.data.model.response.LoginResponse
import com.aoztg.greengrim.data.model.request.SignupRequest
import com.aoztg.greengrim.data.model.response.SignupResponse
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
        @Body params: LoginRequest
    ): Response<LoginResponse>

    @POST("/nick-name")
    suspend fun checkNick(
        @Body params: CheckNickRequest
    ): Response<CheckNickResponse>
}