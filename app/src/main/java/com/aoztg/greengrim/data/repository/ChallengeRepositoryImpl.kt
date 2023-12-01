package com.aoztg.greengrim.data.repository

import com.aoztg.greengrim.data.model.BaseState
import com.aoztg.greengrim.data.model.request.CreateChallengeRequest
import com.aoztg.greengrim.data.model.response.ChallengeDetailResponse
import com.aoztg.greengrim.data.model.response.ChallengeListResponse
import com.aoztg.greengrim.data.model.response.CreateChallengeResponse
import com.aoztg.greengrim.data.model.runRemote
import com.aoztg.greengrim.data.remote.ChallengeAPI
import javax.inject.Inject

class ChallengeRepositoryImpl @Inject constructor(private val api: ChallengeAPI) :
    ChallengeRepository {

    override suspend fun createChallenge(data: CreateChallengeRequest): BaseState<CreateChallengeResponse> =
        runRemote(api.createChallenge(data))

    override suspend fun getChallengeList(
        category: String,
        page: Int,
        size: Int,
        sort: String
    ): BaseState<ChallengeListResponse> =
        runRemote(api.getChallengeList(category, page, size, sort))

    override suspend fun getChallengeDetail(id: Int): BaseState<ChallengeDetailResponse> =
        runRemote(api.getChallengeDetail(id))

    override suspend fun getMyChallenge(
        page: Int,
        size: Int,
        sort: String
    ): BaseState<ChallengeListResponse> = runRemote(api.getMyChallengeList(page, size, sort))

    override suspend fun exitChallenge(id: Int): BaseState<Unit> = runRemote(api.exitChallenge(id))
}