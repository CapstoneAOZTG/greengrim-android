package com.aoztg.greengrim.data.remote

import com.aoztg.greengrim.data.model.ChallengeListResponse
import com.aoztg.greengrim.data.model.GetProfileResponse
import com.aoztg.greengrim.data.model.PatchProfileRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Query

interface InfoAPI {

    @GET("/visitor/profile")
    suspend fun getProfile() : Response<GetProfileResponse>

    @PATCH("/visitor/profile")
    suspend fun patchProfile(
        @Body params: PatchProfileRequest
    ): Response<Unit>

    @DELETE("/visitor/delete")
    suspend fun withdrawal(): Response<Unit>

    @GET("/visitor/challenges")
    suspend fun getMyChallengeList(
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("sort") sort: String
    ): Response<ChallengeListResponse>
}