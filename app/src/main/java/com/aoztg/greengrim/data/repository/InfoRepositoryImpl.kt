package com.aoztg.greengrim.data.repository

import com.aoztg.greengrim.data.model.BaseState
import com.aoztg.greengrim.data.model.request.PatchProfileRequest
import com.aoztg.greengrim.data.model.response.GetProfileResponse
import com.aoztg.greengrim.data.model.runRemote
import com.aoztg.greengrim.data.remote.InfoAPI
import javax.inject.Inject

class InfoRepositoryImpl @Inject constructor(private val api: InfoAPI) : InfoRepository {

    override suspend fun getProfile(): BaseState<GetProfileResponse> =
        runRemote { api.getProfile() }

    override suspend fun patchProfile(data: PatchProfileRequest): BaseState<Unit> =
        runRemote { api.patchProfile(data) }

    override suspend fun withdrawal(): BaseState<Unit> = runRemote { api.withdrawal() }

}