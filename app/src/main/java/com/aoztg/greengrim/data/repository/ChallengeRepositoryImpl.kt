package com.aoztg.greengrim.data.repository

import com.aoztg.greengrim.data.model.CreateChallengeRequest
import com.aoztg.greengrim.data.remote.ChallengeAPI
import retrofit2.Response
import javax.inject.Inject

class ChallengeRepositoryImpl @Inject constructor(private val api: ChallengeAPI): ChallengeRepository {

    override suspend fun createChallenge(data: CreateChallengeRequest): Response<Unit> = api.createChallenge(data)

}