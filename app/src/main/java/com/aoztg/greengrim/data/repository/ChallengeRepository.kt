package com.aoztg.greengrim.data.repository

import com.aoztg.greengrim.data.model.BaseState
import com.aoztg.greengrim.data.model.request.CreateChallengeRequest
import com.aoztg.greengrim.data.model.response.ChallengeDetailResponse
import com.aoztg.greengrim.data.model.response.ChallengeListResponse
import com.aoztg.greengrim.data.model.response.CreateChallengeResponse

interface ChallengeRepository {

    suspend fun createChallenge(
        data: CreateChallengeRequest
    ): BaseState<CreateChallengeResponse>

    suspend fun getChallengeDetail(
        id: Long
    ): BaseState<ChallengeDetailResponse>

    suspend fun getChallengeList(
        category: String,
        page: Int,
        size: Int,
        sort: String
    ): BaseState<ChallengeListResponse>

    suspend fun getMyChallenge(
        page: Int,
        size: Int,
        sort: String
    ): BaseState<ChallengeListResponse>

    suspend fun exitChallenge(
        id: Long
    ): BaseState<Unit>

    suspend fun getRandomKeywords(): BaseState<List<String>>

    suspend fun postPoint(): BaseState<Unit>
}