package com.aoztg.greengrim.data.remote

import com.aoztg.greengrim.data.model.response.GetProfileResponse
import com.aoztg.greengrim.data.model.request.PatchProfileRequest
import com.aoztg.greengrim.data.model.response.MyInfoResponse
import com.aoztg.greengrim.data.model.response.MyKeywordsResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH

interface InfoAPI {

    @GET("/visitor/profile")
    suspend fun getProfile() : Response<GetProfileResponse>

    @PATCH("/visitor/profile")
    suspend fun patchProfile(
        @Body params: PatchProfileRequest
    ): Response<Unit>

    @DELETE("/visitor/delete")
    suspend fun withdrawal(): Response<Unit>

    @GET("/visitor/my")
    suspend fun getMyInfo(): Response<MyInfoResponse>

    @GET("/visitor/keywords")
    suspend fun getMyKeywords(): Response<MyKeywordsResponse>

}