package com.aoztg.greengrim.data.repository

import com.aoztg.greengrim.data.model.ChallengeDetailResponse
import com.aoztg.greengrim.data.model.ChallengeListResponse
import com.aoztg.greengrim.data.model.CreateChallengeRequest
import com.aoztg.greengrim.data.remote.ChallengeAPI
import retrofit2.Response
import javax.inject.Inject

class ChallengeRepositoryImpl @Inject constructor(private val api: ChallengeAPI): ChallengeRepository {

    override suspend fun createChallenge(data: CreateChallengeRequest): Response<Unit> = api.createChallenge(data)

    override suspend fun getChallengeList(
        category: String,
        page: Int,
        size: Int,
        sort: String
    ): Response<ChallengeListResponse> = api.getChallengeList(category, page, size, sort)

    override suspend fun getChallengeDetail(id: Int): Response<ChallengeDetailResponse> = api.getChallengeDetail(id)

}