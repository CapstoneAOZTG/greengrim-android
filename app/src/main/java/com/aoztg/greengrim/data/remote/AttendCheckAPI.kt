package com.aoztg.greengrim.data.remote

import com.aoztg.greengrim.data.model.request.VerificationsRequest
import com.aoztg.greengrim.data.model.response.CertificationDetailResponse
import com.aoztg.greengrim.data.model.response.ChallengeDetailResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AttendCheckAPI {

    @POST("/visitor/verifications")
    suspend fun verifyCertification(
        @Body params: VerificationsRequest
    ): Response<Unit>

    @GET("/visitor/verifications")
    suspend fun getCertificationForVerify(): Response<CertificationDetailResponse>
}