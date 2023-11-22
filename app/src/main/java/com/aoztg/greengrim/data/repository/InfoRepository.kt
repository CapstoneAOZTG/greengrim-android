package com.aoztg.greengrim.data.repository

import com.aoztg.greengrim.data.model.response.GetProfileResponse
import com.aoztg.greengrim.data.model.request.PatchProfileRequest
import retrofit2.Response

interface InfoRepository {

    suspend fun getProfile(): Response<GetProfileResponse>

    suspend fun patchProfile(
        data: PatchProfileRequest
    ): Response<Unit>

    suspend fun withdrawal(): Response<Unit>
}