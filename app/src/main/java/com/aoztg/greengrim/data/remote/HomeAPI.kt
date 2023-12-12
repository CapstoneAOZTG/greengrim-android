package com.aoztg.greengrim.data.remote

import com.aoztg.greengrim.data.model.response.HomeInfoResponse
import com.aoztg.greengrim.data.model.response.HotChallengeResponse
import retrofit2.Response
import retrofit2.http.GET

interface HomeAPI {

    @GET("/home/challenges")
    suspend fun getHotChallenges(): Response<HotChallengeResponse>

    @GET("/visitor/home")
    suspend fun getHomeInfo(): Response<HomeInfoResponse>
}