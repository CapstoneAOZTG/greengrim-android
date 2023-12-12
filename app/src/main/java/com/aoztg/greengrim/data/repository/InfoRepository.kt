package com.aoztg.greengrim.data.repository

import com.aoztg.greengrim.data.model.BaseState
import com.aoztg.greengrim.data.model.request.PatchProfileRequest
import com.aoztg.greengrim.data.model.response.GetProfileResponse
import com.aoztg.greengrim.data.model.response.MyInfoResponse
import com.aoztg.greengrim.data.model.response.MyKeywordsResponse

interface InfoRepository {

    suspend fun getProfile(): BaseState<GetProfileResponse>

    suspend fun patchProfile(
        data: PatchProfileRequest
    ): BaseState<Unit>

    suspend fun withdrawal(): BaseState<Unit>

    suspend fun getMyInfo(): BaseState<MyInfoResponse>

    suspend fun getMyKeywords(): BaseState<MyKeywordsResponse>
}