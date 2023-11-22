package com.aoztg.greengrim.data.repository

import com.aoztg.greengrim.data.model.response.ChallengeDetailResponse
import com.aoztg.greengrim.data.model.response.ChallengeListResponse
import com.aoztg.greengrim.data.model.request.CreateChallengeRequest
import com.aoztg.greengrim.data.model.response.CreateChallengeResponse
import com.aoztg.greengrim.data.remote.ChallengeAPI
import retrofit2.Response
import javax.inject.Inject

class ChallengeRepositoryImpl @Inject constructor(private val api: ChallengeAPI): ChallengeRepository {

    override suspend fun createChallenge(data: CreateChallengeRequest): Response<CreateChallengeResponse> = api.createChallenge(data)

    override suspend fun getChallengeList(
        category: String,
        page: Int,
        size: Int,
        sort: String
    ): Response<ChallengeListResponse> = api.getChallengeList(category, page, size, sort)

    override suspend fun getChallengeDetail(id: Int): Response<ChallengeDetailResponse> = api.getChallengeDetail(id)
    override suspend fun getMyChallenge(
        page: Int,
        size: Int,
        sort: String
    ): Response<ChallengeListResponse> = api.getMyChallengeList(page,size,sort)
}