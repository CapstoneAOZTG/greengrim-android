package com.aoztg.greengrim.data.repository

import com.aoztg.greengrim.data.network.loginnetwork.LoginAPI
import com.aoztg.greengrim.data.network.loginnetwork.model.SignupPostData
import javax.inject.Inject

class IntroRepositoryImpl @Inject constructor(private val api : LoginAPI) : IntroRepository {

    override suspend fun postSignup(data: SignupPostData): Boolean {

        return false
    }

}