package com.aoztg.greengrim.data.repository

import com.aoztg.greengrim.data.model.BaseState
import com.aoztg.greengrim.data.model.request.CheckNickRequest
import com.aoztg.greengrim.data.model.request.LoginRequest
import com.aoztg.greengrim.data.model.request.SignupRequest
import com.aoztg.greengrim.data.model.response.CheckNickResponse
import com.aoztg.greengrim.data.model.response.LoginResponse
import com.aoztg.greengrim.data.model.response.SignupResponse
import com.aoztg.greengrim.data.model.runRemote
import com.aoztg.greengrim.data.remote.IntroAPI
import javax.inject.Inject

class IntroRepositoryImpl @Inject constructor(private val api: IntroAPI) : IntroRepository {

    override suspend fun signup(data: SignupRequest): BaseState<SignupResponse> =
        runRemote { api.signup(data) }

    override suspend fun login(data: LoginRequest): BaseState<LoginResponse> =
        runRemote { api.login(data) }

    override suspend fun checkNick(data: CheckNickRequest): BaseState<CheckNickResponse> =
        runRemote { api.checkNick(data) }

}