package com.aoztg.greengrim.data.repository

import com.aoztg.greengrim.data.model.response.HomeInfoResponse
import com.aoztg.greengrim.data.model.response.HotChallengeResponse
import com.aoztg.greengrim.data.remote.HomeAPI
import retrofit2.Response
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(
    private val api: HomeAPI
) : HomeRepository {

    override suspend fun getHotChallenges(): Response<HotChallengeResponse> = api.getHotChallenges()
    override suspend fun getHomeInfo(): Response<HomeInfoResponse> = api.getHomeInfo()
}