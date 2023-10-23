package com.aoztg.greengrim.data.repository

import com.aoztg.greengrim.data.model.CheckNickRequest
import com.aoztg.greengrim.data.model.CheckNickResponse
import com.aoztg.greengrim.data.model.LoginRequest
import com.aoztg.greengrim.data.model.LoginResponse
import com.aoztg.greengrim.data.model.SignupRequest
import com.aoztg.greengrim.data.model.SignupResponse
import com.aoztg.greengrim.data.remote.IntroAPI
import retrofit2.Response
import javax.inject.Inject

class IntroRepositoryImpl @Inject constructor(private val api: IntroAPI) : IntroRepository {

    override suspend fun signup(data: SignupRequest): Response<SignupResponse> = api.signup(data)
    override suspend fun login(data: LoginRequest): Response<LoginResponse> = api.login(data)
    override suspend fun checkNick(data: CheckNickRequest): Response<CheckNickResponse> = api.checkNick(data)

}