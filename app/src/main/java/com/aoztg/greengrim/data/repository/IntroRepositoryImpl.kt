package com.aoztg.greengrim.data.repository

import com.aoztg.greengrim.data.remote.IntroAPI
import com.aoztg.greengrim.data.model.SignupPostData
import javax.inject.Inject

class IntroRepositoryImpl @Inject constructor(private val api : IntroAPI) : IntroRepository {

    override suspend fun postSignup(data: SignupPostData): Boolean {

        return false
    }

}