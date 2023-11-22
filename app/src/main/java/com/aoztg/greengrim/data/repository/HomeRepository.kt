package com.aoztg.greengrim.data.repository

import com.aoztg.greengrim.data.model.response.HotChallengeResponse
import retrofit2.Response

interface HomeRepository {
    suspend fun getHotChallenges(): Response<HotChallengeResponse>
}