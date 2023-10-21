package com.aoztg.greengrim.data.repository

import com.aoztg.greengrim.data.model.LoginRequest
import com.aoztg.greengrim.data.model.LoginResponse
import com.aoztg.greengrim.data.model.SignupRequest
import com.aoztg.greengrim.data.model.SignupResponse
import retrofit2.Response

interface IntroRepository {

    suspend fun signup(
        data: SignupRequest
    ): Response<SignupResponse>

    suspend fun login(
        data: LoginRequest
    ): Response<LoginResponse>
}