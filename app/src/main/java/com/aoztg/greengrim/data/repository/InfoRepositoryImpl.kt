package com.aoztg.greengrim.data.repository

import com.aoztg.greengrim.data.model.response.GetProfileResponse
import com.aoztg.greengrim.data.model.request.PatchProfileRequest
import com.aoztg.greengrim.data.remote.InfoAPI
import retrofit2.Response
import javax.inject.Inject

class InfoRepositoryImpl @Inject constructor(private val api: InfoAPI): InfoRepository {

    override suspend fun getProfile(): Response<GetProfileResponse> = api.getProfile()
    override suspend fun patchProfile(data: PatchProfileRequest): Response<Unit> = api.patchProfile(data)
    override suspend fun withdrawal(): Response<Unit> = api.withdrawal()

}