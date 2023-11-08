package com.aoztg.greengrim.data.remote

import com.aoztg.greengrim.data.model.CreateChallengeRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ChallengeAPI {

    @POST("/visitor/challenges")
    suspend fun createChallenge(
        @Body params: CreateChallengeRequest
    ): Response<Unit>
}