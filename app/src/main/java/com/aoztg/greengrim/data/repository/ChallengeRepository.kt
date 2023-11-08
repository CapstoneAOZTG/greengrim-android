package com.aoztg.greengrim.data.repository

import com.aoztg.greengrim.data.model.ChallengeDetailResponse
import com.aoztg.greengrim.data.model.ChallengeListResponse
import com.aoztg.greengrim.data.model.CreateChallengeRequest
import retrofit2.Response

interface ChallengeRepository {

    suspend fun createChallenge(
        data: CreateChallengeRequest
    ): Response<Unit>

    suspend fun getChallengeDetail(
        id: Int
    ): Response<ChallengeDetailResponse>

    suspend fun getChallengeList(
        category: String,
        page: Int,
        size: Int,
        sort: String
    ): Response<ChallengeListResponse>
}