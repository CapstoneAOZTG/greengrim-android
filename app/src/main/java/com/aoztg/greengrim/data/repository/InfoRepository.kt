package com.aoztg.greengrim.data.repository

import com.aoztg.greengrim.data.model.ChallengeListResponse
import com.aoztg.greengrim.data.model.GetProfileResponse
import com.aoztg.greengrim.data.model.PatchProfileRequest
import retrofit2.Response

interface InfoRepository {

    suspend fun getProfile(): Response<GetProfileResponse>

    suspend fun patchProfile(
        data: PatchProfileRequest
    ): Response<Unit>

    suspend fun withdrawal(): Response<Unit>

    suspend fun getMyChallengeList(
        page: Int,
        size: Int,
        sort: String
    ): Response<ChallengeListResponse>
}