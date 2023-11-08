package com.aoztg.greengrim.data.repository

import com.aoztg.greengrim.data.model.CreateChallengeRequest
import retrofit2.Response

interface ChallengeRepository {

    suspend fun createChallenge(
        data: CreateChallengeRequest
    ): Response<Unit>
}