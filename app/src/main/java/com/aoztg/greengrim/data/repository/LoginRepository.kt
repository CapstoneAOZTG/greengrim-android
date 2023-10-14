package com.aoztg.greengrim.data.repository

import com.aoztg.greengrim.data.network.loginnetwork.model.SignupPostData

interface LoginRepository {

    suspend fun postSignup(
        data : SignupPostData
    ) : Boolean
}