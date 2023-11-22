package com.aoztg.greengrim.data.repository

import com.aoztg.greengrim.data.model.request.CheckNickRequest
import com.aoztg.greengrim.data.model.response.CheckNickResponse
import com.aoztg.greengrim.data.model.request.LoginRequest
import com.aoztg.greengrim.data.model.response.LoginResponse
import com.aoztg.greengrim.data.model.request.SignupRequest
import com.aoztg.greengrim.data.model.response.SignupResponse
import retrofit2.Response

interface IntroRepository {

    suspend fun signup(
        data: SignupRequest
    ): Response<SignupResponse>

    suspend fun login(
        data: LoginRequest
    ): Response<LoginResponse>

    suspend fun checkNick(
        data: CheckNickRequest
    ): Response<CheckNickResponse>
}