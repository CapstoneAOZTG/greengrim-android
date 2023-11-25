package com.aoztg.greengrim.data.repository

import com.aoztg.greengrim.data.model.request.VerificationsRequest
import com.aoztg.greengrim.data.model.response.CertificationDetailResponse
import retrofit2.Response

interface AttendCheckRepository {

    suspend fun verifyCertification(
        data: VerificationsRequest
    ): Response<Unit>

    suspend fun getCertificationForVerify(): Response<CertificationDetailResponse>
}