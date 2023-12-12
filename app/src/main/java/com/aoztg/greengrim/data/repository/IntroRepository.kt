package com.aoztg.greengrim.data.repository

import com.aoztg.greengrim.data.model.BaseState
import com.aoztg.greengrim.data.model.request.CheckNickRequest
import com.aoztg.greengrim.data.model.request.LoginRequest
import com.aoztg.greengrim.data.model.request.SignupRequest
import com.aoztg.greengrim.data.model.response.CheckNickResponse
import com.aoztg.greengrim.data.model.response.LoginResponse
import com.aoztg.greengrim.data.model.response.SignupResponse

interface IntroRepository {

    suspend fun signup(
        data: SignupRequest
    ): BaseState<SignupResponse>

    suspend fun login(
        data: LoginRequest
    ): BaseState<LoginResponse>

    suspend fun checkNick(
        data: CheckNickRequest
    ): BaseState<CheckNickResponse>
}