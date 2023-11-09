package com.aoztg.greengrim.data.remote

import com.aoztg.greengrim.data.model.HotChallengeResponse
import retrofit2.Response
import retrofit2.http.GET

interface HomeAPI {

    @GET("/home/challenges")
    suspend fun getHotChallenges(): Response<HotChallengeResponse>
}