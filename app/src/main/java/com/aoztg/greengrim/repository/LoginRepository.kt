package com.aoztg.greengrim.repository

import com.aoztg.greengrim.network.loginnetwork.model.SignupPostData

interface LoginRepository {

    suspend fun postSignup(
        data : SignupPostData
    ) : Boolean
}