package com.aoztg.greengrim.data.repository

import com.aoztg.greengrim.data.remote.introremote.model.SignupPostData

interface IntroRepository {

    suspend fun postSignup(
        data : SignupPostData
    ) : Boolean
}