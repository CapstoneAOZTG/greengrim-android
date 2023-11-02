package com.aoztg.greengrim.data.repository

import com.aoztg.greengrim.data.model.GetProfileResponse
import com.aoztg.greengrim.data.model.PatchProfileRequest
import retrofit2.Response

interface InfoRepository {

    suspend fun getProfile(): Response<GetProfileResponse>

    suspend fun patchProfile(
        data: PatchProfileRequest
    ): Response<Unit>
}