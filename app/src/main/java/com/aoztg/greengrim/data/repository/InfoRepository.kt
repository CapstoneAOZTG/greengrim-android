package com.aoztg.greengrim.data.repository

import com.aoztg.greengrim.data.model.BaseState
import com.aoztg.greengrim.data.model.request.PatchProfileRequest
import com.aoztg.greengrim.data.model.response.GetProfileResponse

interface InfoRepository {

    suspend fun getProfile(): BaseState<GetProfileResponse>

    suspend fun patchProfile(
        data: PatchProfileRequest
    ): BaseState<Unit>

    suspend fun withdrawal(): BaseState<Unit>
}