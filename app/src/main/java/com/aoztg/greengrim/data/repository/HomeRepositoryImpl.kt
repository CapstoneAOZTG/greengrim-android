package com.aoztg.greengrim.data.repository

import com.aoztg.greengrim.data.model.BaseState
import com.aoztg.greengrim.data.model.response.HomeInfoResponse
import com.aoztg.greengrim.data.model.response.HotChallengeResponse
import com.aoztg.greengrim.data.model.runRemote
import com.aoztg.greengrim.data.remote.HomeAPI
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(
    private val api: HomeAPI
) : HomeRepository {

    override suspend fun getHotChallenges(): BaseState<HotChallengeResponse> =
        runRemote { api.getHotChallenges() }

    override suspend fun getHomeInfo(): BaseState<HomeInfoResponse> =
        runRemote { api.getHomeInfo() }
}