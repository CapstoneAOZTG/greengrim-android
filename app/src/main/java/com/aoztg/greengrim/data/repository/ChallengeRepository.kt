package com.aoztg.greengrim.data.repository

import com.aoztg.greengrim.data.model.response.ChallengeDetailResponse
import com.aoztg.greengrim.data.model.response.ChallengeListResponse
import com.aoztg.greengrim.data.model.request.CreateChallengeRequest
import com.aoztg.greengrim.data.model.response.CreateChallengeResponse
import retrofit2.Response

interface ChallengeRepository {

    suspend fun createChallenge(
        data: CreateChallengeRequest
    ): Response<CreateChallengeResponse>

    suspend fun getChallengeDetail(
        id: Int
    ): Response<ChallengeDetailResponse>

    suspend fun getChallengeList(
        category: String,
        page: Int,
        size: Int,
        sort: String
    ): Response<ChallengeListResponse>

    suspend fun getMyChallenge(
        page: Int,
        size: Int,
        sort: String
    ): Response<ChallengeListResponse>
}