package com.aoztg.greengrim.data.repository

import com.aoztg.greengrim.data.remote.introremote.IntroAPI
import com.aoztg.greengrim.data.remote.introremote.model.SignupPostData
import javax.inject.Inject

class IntroRepositoryImpl @Inject constructor(private val api : IntroAPI) : IntroRepository {

    override suspend fun postSignup(data: SignupPostData): Boolean {

        return false
    }

}