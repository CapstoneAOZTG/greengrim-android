package com.aoztg.greengrim.data.repository

import com.aoztg.greengrim.data.model.BaseState
import com.aoztg.greengrim.data.model.runRemote
import com.aoztg.greengrim.data.remote.FcmAPI
import javax.inject.Inject

class FcmRepositoryImpl @Inject constructor(
    private val api: FcmAPI
): FcmRepository {

    override suspend fun subscribeFcm(): BaseState<Unit> = runRemote(api.subscribeFcm())

    override suspend fun unsubscribeFcm(): BaseState<Unit> = runRemote(api.unsubscribeFcm())
}