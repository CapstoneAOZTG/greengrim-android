package com.aoztg.greengrim.data.repository

import com.aoztg.greengrim.data.model.BaseState
import com.aoztg.greengrim.data.model.response.HomeInfoResponse
import com.aoztg.greengrim.data.model.response.HotChallengeResponse

interface HomeRepository {
    suspend fun getHotChallenges(): BaseState<HotChallengeResponse>
    suspend fun getHomeInfo(): BaseState<HomeInfoResponse>
}